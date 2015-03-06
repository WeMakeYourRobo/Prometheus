package robowarrior;

import robocode.*;
import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Bullet;
import robowarrior.core.EnemyBullet;
import robowarrior.core.FriendlyBullet;
import robowarrior.core.Recorder.Film;
import robowarrior.core.Recorder.Picture;
import robowarrior.core.Utils.MathUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;

public class RadarTracker extends AdvancedRobot {
    int movementDirection = 1;

    boolean hasEnemy=false;
    Film film=new Film();
    Film myFilm=new Film();
    ArrayList<Object> bullets=new ArrayList<Object>();
    private EnemyBot Opfer = null;
    private Rectangle2D rect =new Rectangle2D.Double(0,0,100,100);
    double fieldHeight=0;
    double fieldWidth=0;

    public void run() {
        fieldHeight=getBattleFieldHeight();
        fieldWidth=getBattleFieldWidth();
        //Alles detachen
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

    }

   public void onScannedRobot(ScannedRobotEvent event) {
       this.hasEnemy=true;

       //Längseite zum Gegner drehen , um besser ausweichen zu können
       setTurnRight(event.getBearing()+90);
      // setTurnRadarRight((getHeading() - getRadarHeading() + event.getBearing()));
       if (Opfer != null) {
           //Energieverlust handeln
           handleEnergyLoss(Opfer,event);
           Opfer.update(event,this);
       } else {
           Opfer = new EnemyBot(event,this);
       }
       //Snapshot des Gegners in den Film aufnehmen
       takePicture(Opfer);
       //Angriff
       attackEnemy();

    }

    @Override
    public void onBattleEnded(BattleEndedEvent event){
     //Save Logs
        PrintStream w = null;
        try {
            w = new PrintStream(new RobocodeFileOutputStream(getDataFile("log.dat")));

            for (String line : film.serialize())
            {
                out.println(line);

                w.println(line);

            }

            if (w.checkError()) {
                out.println("I could not write the log!");
            }
        } catch (IOException e) {
            out.println("IOException trying to write: ");
            e.printStackTrace(out);
        } finally {
            if (w != null) {
                w.close();
            }
        }


    }

    @Override
    public void onStatus(StatusEvent e) {
        makeSelfie();
        //Scanne
          scanning();
        //Bullets bei jedem Tick updaten
        Graphics2D g= getGraphics();

        for(int i=bullets.size()-1; i> -1; i--){
           // Bullet bullet = (Bullet) object
            Object object=bullets.get(i);
            if ( object instanceof EnemyBullet){
                EnemyBullet bullet=(EnemyBullet) object;
                g.setColor(new Color(0,255, 57));
                updateBullet(bullet,g);
            } else if(object instanceof FriendlyBullet) {
                FriendlyBullet bullet=(FriendlyBullet) object;
                g.setColor(new Color(24, 171, 255));
                updateBullet(bullet,g);
            }

        }

        g.setColor(new Color(0,255, 57));
        film.draw(g);
        g.setColor(new Color(24, 171, 255));
        myFilm.draw(g);




    }

    private void updateBullet(Bullet bullet, Graphics2D g){
        bullet.update();
        double[] xy=bullet.getCoords();
        g.drawRect((int) xy[0] - 3, (int) xy[1] - 3, 6, 6);
        //Lösche Bullet wenn es gelöscht werden soll
        if (bullet.selfdestroy == true)
        {
            bullets.remove(bullet);
        }
    }

    private void makeSelfie(){
        Picture pic =new Picture(getTime());
        pic.setHeading(getHeading());
        pic.setVelocity(getVelocity());
        pic.setX(getX());
        pic.setY(getY());
        myFilm.addPicture(pic);
    }

    private void takePicture(EnemyBot Opfer){
        Picture pic =new Picture(getTime());
        pic.setHeading(Opfer.getHeading());
        pic.setVelocity(Opfer.getVelocity());
        pic.setX(Opfer.getX());
        pic.setY(Opfer.getY());
        film.addPicture(pic);
    }

    private void handleEnergyLoss(EnemyBot Opfer,ScannedRobotEvent event){
        double changeInEnergy= Opfer.getEnergy() - event.getEnergy();
        // Wenn Energie gefallen ist ausweichen
        if(changeInEnergy > 0 && changeInEnergy <= 3) {
            this.bullets.add(new EnemyBullet(event.getBearing(),changeInEnergy,event.getDistance(),fieldWidth,fieldHeight,getX(),getY(),getHeading()));
            setAhead((event.getDistance() / 8 + 15) * movementDirection);
            movementDirection=movementDirection*-1;
        }
    }
    private void attackEnemy(){
        double absoluteBearing = getHeading() + Opfer.getBearing();
        double firePower = Math.min(400 / Opfer.getDistance(), 3);

        double timeToHit=Opfer.getDistance()/firePower;
        double enemyTravelDistance=Opfer.getVelocity()*timeToHit;
        double futureX=Opfer.getFutureX(enemyTravelDistance);
        double futureY=Opfer.getFutureY(enemyTravelDistance);
        Graphics2D g=getGraphics();
        g.drawOval((int)futureX,(int)futureY,2,2);
        double absDeg = MathUtils.absoluteBearing(new Point2D.Double(getX(), getY()), new Point2D.Double(futureX, futureY));
        double bearingFromGun =MathUtils.normalRelativeAngle(absDeg - getGunHeading());
        if (Math.abs(bearingFromGun) <= 13) {
            setTurnGunRight(bearingFromGun);

            if (getGunHeat() == 0) {
                double firepower= Math.min(400 / Opfer.getDistance(), 3);
                setFire(firePower);
                this.bullets.add(new FriendlyBullet(MathUtils.absoluteBearing( new Point2D.Double(Opfer.getX(), Opfer.getY()),new Point2D.Double(getX(), getY())),firepower,0,fieldWidth,fieldHeight,Opfer.getX(),Opfer.getY(), Opfer.getHeading()));

            }
        } else {
            setTurnGunRight(bearingFromGun);
        }

    }

    void scanning() {
        double radarOffset;
        if (!hasEnemy) {
            radarOffset = 360;
        } else {
            double absBearing=MathUtils.absoluteBearing(new Point2D.Double(getX(), getY()), new Point2D.Double(Opfer.getX(), Opfer.getY()));

            radarOffset = MathUtils.normalRelativeAngle(getRadarHeading() - absBearing) ;

            if (radarOffset < 0) {
                radarOffset -= 5;
            } else {
                radarOffset += 5;
            }
        }

        setTurnRadarLeft(radarOffset);
    }








}
