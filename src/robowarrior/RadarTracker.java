package robowarrior;

import robocode.*;
import robocode.util.Utils;
import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Bullet;
import robowarrior.core.EnemyBullet;
import robowarrior.core.FriendlyBullet;
import robowarrior.core.Recorder.Film;
import robowarrior.core.Recorder.Picture;
import robowarrior.core.Utils.MathUtils;
import robowarrior.core.Welle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RadarTracker extends AdvancedRobot {
    int movementDirection = 1;

    boolean hasEnemy=false;
    Film film=new Film();
    Film myFilm=new Film();
    ArrayList<Object> bullets=new ArrayList<Object>();

    private EnemyBot Opfer = null;
    double fieldHeight=0;
    double fieldWidth=0;

    double lastEnemyVelocity=0;


    public void run() {
        fieldHeight=getBattleFieldHeight();
        fieldWidth=getBattleFieldWidth();
        //Alles detachen
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
       Opfer.setEnergy(event.getEnergy());
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {

    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {

    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
      //This should never happen
      // But if then
      setTurnRadarRightRadians(getHeadingRadians() - event.getBullet().getHeadingRadians());
    }

    public void onScannedRobot(ScannedRobotEvent event) {
       this.hasEnemy=true;

       //Längseite zum Gegner drehen , um besser ausweichen zu können
       setTurnRightRadians(event.getBearingRadians()+Math.PI/2);

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

    }

    @Override
    public void onStatus(StatusEvent e) {
            makeSelfie();
        //Scanne
          scanning();
        // Bewege dich
        move();
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
    private void move(){
        double xForce=0, yForce= 0;
        for(int i=0;i< bullets.size() ;i++){
            Bullet bullet=(Bullet) bullets.get(i);
            double[] coords=bullet.getCoords();
            Point2D.Double badPoint= new Point2D.Double(coords[0],coords[1]);
            double absBearing=Utils.normalAbsoluteAngle(Math.atan2(badPoint.x-getX(),badPoint.y-getY()));
            double distance=badPoint.distance(getX(),getY());
            if(distance<=200){
            xForce -= Math.sin(absBearing) / (distance * distance);
            yForce -= Math.cos(absBearing) / (distance * distance);
            }
        }
        double angle = Math.atan2(xForce, yForce);
        if(xForce == 0 && yForce == 0) {
            // If no force, do nothing
        } else if(Math.abs(angle-getHeadingRadians())<Math.PI/2){
            setTurnRightRadians(Utils.normalRelativeAngle(angle-getHeadingRadians()));
            setAhead(Double.POSITIVE_INFINITY);
        } else {
            setTurnRightRadians(Utils.normalRelativeAngle(angle+Math.PI-getHeadingRadians()));
            setAhead(Double.NEGATIVE_INFINITY);
        }
    }
    private void updateBullet(Bullet bullet, Graphics2D g){
        bullet.update();
        double[] xy=bullet.getCoords();
        g.drawRect((int) xy[0] - 3, (int) xy[1] - 3, 6, 6);
        //Lösche Bullet wenn es gelöscht werden soll
        if (bullet.selfdestroy)
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
            this.bullets.add(new EnemyBullet(event.getBearingRadians(),changeInEnergy,event.getDistance(),fieldWidth,fieldHeight,getX(),getY(),getHeadingRadians()));
          //  setAhead((event.getDistance() / 8 + 15) * movementDirection);
          //  movementDirection=movementDirection*-1;
        }
    }
    private void attackEnemy(){
        double enemyAbsoluteBearing = getHeadingRadians() + Opfer.getBearing();
        double enemyDistance = Opfer.getDistance();
        double enemyVelocity = Opfer.getVelocity();
        double lateralDirection = 1;
        if (enemyVelocity != 0) {
            lateralDirection = MathUtils.sign(enemyVelocity * Math.sin(Opfer.getHeading() - enemyAbsoluteBearing));
        }
        Welle wave = new Welle(this);
        wave.gunLocation = new Point2D.Double(getX(), getY());
        Welle.targetLocation = MathUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
        wave.lateralDirection = lateralDirection;
        wave.bulletPower = 3;
        wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
        lastEnemyVelocity = enemyVelocity;
        wave.bearing = enemyAbsoluteBearing;
        setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
        setFire(wave.bulletPower);
        if (getEnergy() >= wave.bulletPower) {
            addCustomEvent(wave);
        }
        //setTurnRadarRight(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeading()) * 2);
    }


    void scanning() {
        double radarOffset;
        if (!hasEnemy) {
            radarOffset = 360;
        } else {
            double absBearing=MathUtils.absoluteBearing(new Point2D.Double(getX(), getY()), new Point2D.Double(Opfer.getX(), Opfer.getY()));

            radarOffset = MathUtils.normalRelativeAngle(getRadarHeadingRadians() - absBearing) ;

            if (radarOffset < 0) {
                radarOffset -= Math.PI/8;
            } else {
                radarOffset += Math.PI/8;
            }
        }

        setTurnRadarLeftRadians(radarOffset);
    }







}
