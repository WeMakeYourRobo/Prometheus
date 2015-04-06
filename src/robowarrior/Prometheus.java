package robowarrior;

import robocode.*;
import robocode.util.Utils;
import robowarrior.core.*;
import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Bullet;
import robowarrior.core.Utils.MathUtils;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Jens Laur, Markus Krabbenhöft, Dennis Pries
 * @version 1.0
 * Hauptklasse des Roboters
 */

public class Prometheus extends AdvancedRobot {
    // eine Instanz von Zufall ist immer gut
    Random R= new Random();
    // Initial werden wir wohl keinen Gegner haben
    boolean hasEnemy=false;

    // Sollen die Stats in Datei gespeichert und wieder ausgelesen werden?
    // Abgeschaltet da Laden der Datei unperformant und beim Rundensystem nicht brauchbar
    final static boolean USE_SAVING=false;
    // Unsere Sammlung an Bullets (eigene, sowie auch fremde)
    ArrayList<Object> bullets=new ArrayList<Object>();
    // Letzte Velocity des Gegners
    double lastVelocity=0;
   // Static damit wir zwischen den Runden speichern
    // 41 -> max ScannerDistanz(1200px) /30=40 also 41 Indieces  ( Distanzen zusammenfassen die eine Differenz von +- 20 haben )
    // 4 -> abs() von PI =3 also 4 Indieces ( wegen radianten )
    // 9 -> max Velocity=8 also 9 Indieces ( jetzige geschwindigkeit )
    // 9 -> max Velocity=8 also 9 Indieces ( alte geschwindigkeit )
    // 31 -> Anzahl der Guess Factors
    static int[][][][][] stats = new int[41][4][9][9][31];
    static boolean hasLoadedStats=false;
    // Unser Gegner fährt initial immer vorwärts
    int direction = 1;
    // Auch wir fahren initial immer vorwärts
    int movementDirection = 1;

    // Unser Gegner
    private EnemyBot Opfer = null;
    // Breite und Höhe des Feldes wird man sicherlich mal brauchen
    double fieldHeight=0;
    double fieldWidth=0;
    // Virtuelle Wand
    Rectangle2D rect;

    public void run() {
        // Setze Höhe und Breite des Spielfeldes
        fieldHeight = getBattleFieldHeight();
        fieldWidth = getBattleFieldWidth();
        rect= new Rectangle2D.Double(50,50,getBattleFieldWidth()-50,getBattleFieldHeight()-50);
        // Verhindert, dass sich irgend ein Teil des Roboter abhängig von einem anderen bewegt
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

    }

    @Override
    // Setzt die aktuelle Energie des Gegner neu um zu verhindern,
    // dass wir einem nicht vorhandenen Bullet ausweichen, das wir selbst erschaffen haben
    public void onBulletHit(BulletHitEvent event) {
       Opfer.setEnergy(event.getEnergy());
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
           Opfer = new EnemyBot(event, this);

       }

       //Angriff
       attackEnemy();

        if(!hasLoadedStats && hasEnemy && USE_SAVING){
            hasLoadedStats = true;
            // Wenn wir schonmal gegen diesen Robo gekämpft haben, können wir unsere damaligen Stats verwenden
            Object content = FileHandler.readCompressedObject(getDataFile(Opfer.getName() + ".rbwarriorlog"));
            if (content != null) {
                stats = (int[][][][][]) content;

            }
        }
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event){
       FileHandler.writeObject(stats,getDataFile(Opfer.getName()+".rbwarriorlog"));
    }

    @Override
    /**
     * Wird jeden Tick von Robocode ausgeführt
     * Ersetz bei uns die while loop in der run Methode
     */
    public void onStatus(StatusEvent e) {

        //Scanne
          scanning();
        // Bewege dich
          move();
        //Bullets bei jedem Tick updaten und wenn aktiviert auf den Screen malen
        Graphics2D g= getGraphics();
        for(int i=bullets.size()-1; i> -1; i--){
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



    }

    /**
     * Wir gehen durch das Array mit den Bullets, die auf uns abgefeurt worden sind und weichen erst aus wenn wir müssen
     */
    private void move(){
        // Ticks, die wir brauchen um zu auszuweichen + 3 Ticks zur Sicherheit
        double movementTicks=(getWidth()/2)+3;
        for (Object object : bullets) {
            if (object instanceof EnemyBullet) {
                EnemyBullet bullet = (EnemyBullet) object;
                double[] coords = bullet.getCoords();
                Point2D.Double badPoint = new Point2D.Double(coords[0], coords[1]);
                double distance = badPoint.distance(getX(), getY());
                //Tick die das Bullet braucht um uns zu Treffen
                double ticks=distance/bullet.getSpeed();
                // Müssen wir ausweichen?
                if(ticks<=movementTicks){
                    setAhead(movementTicks*movementDirection);
                }
            }
        }

    }
    // Update das Bullet  und löschen es gegebenen falls
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

    // Handelt was passiert wenn die Energie des Gegners gefallen ist
    private void handleEnergyLoss(EnemyBot Opfer,ScannedRobotEvent event){
        double changeInEnergy= Opfer.getEnergy() - event.getEnergy();
        // Wenn Energie gefallen ist ein Bullet in die Sammlung aufnehmen
        // Ein Schuss kann maximal die Energie von 3 haben,
        if(changeInEnergy > 0 && changeInEnergy <= 3) {
            this.bullets.add(new EnemyBullet(event.getBearingRadians(),changeInEnergy,event.getDistance(),fieldWidth,fieldHeight,getX(),getY(),getHeadingRadians()));
        }
    }
    // Den Gegner angreifen
    private void attackEnemy(){
        // Werte für die Wave Klasse update
        Wave.me=this;
        Wave.enemyX=Opfer.getX();
        Wave.enemyY=Opfer.getY();
        Wave.now=getTime();


        //Absoluter Winkel zum Gegner
        double absoluteBearing = getHeadingRadians() + Opfer.getBearing();
        // Berechne Bullet Power abhängig von Distanz
        double power = Math.min(400 / Opfer.getDistance(), 3);

        //Fährt das Opfer rückwärts?
        if (Opfer.getVelocity() != 0){
            if (Math.sin(Opfer.getHeading()-absoluteBearing) * Opfer.getVelocity() < 0){
                this.direction = -1;
            }else{
                this.direction = 1;
            }
        }
        //Passed by Reference
        // nur die Statistiken wo die Distance
        int[] currentStats = stats[(int)Opfer.getDistance()/30][(int) Math.abs(Opfer.getBearing())][(int)Math.abs(Opfer.getVelocity())][(int)Math.abs(lastVelocity)];
        lastVelocity=Opfer.getVelocity();
        Wave newWave = new Wave(getX(), getY(), absoluteBearing, power, direction, getTime(), currentStats);
        addCustomEvent(newWave);

        // 15 ist die Mitte unseres Array also GuessFactor = 0
        int bestIndex = 15;
        // Bekomme Index mit den meisten Hits
        for (int i = 0; i < currentStats.length; i++){
            if (currentStats[bestIndex] < currentStats[i])
                bestIndex = i;
        }
        // Rechne Index zurück in Guess Factor
        double guessFactor = (double)(bestIndex - (currentStats.length - 1) / 2) / ((currentStats.length -1) / 2);
        //Rechne Guessfactor zurück in Winkel
        double angleOffset = direction * guessFactor * MathUtils.maxEscapeAngle(power);

            //Ziele auf neue Position
            setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians() + angleOffset));
        //Feuer
            setFire(power);
        //Unser Bullet hinzufügen
            bullets.add(new FriendlyBullet(getGunHeadingRadians(),power,0,fieldWidth,fieldHeight,getX(),getY(),getHeadingRadians()));



    }

    /**
     * Scan Methode
     * Scannt rundherum wenn kein Gegner existiert und osszilliert um den Gegner wenn wir einem haben, damit er uns ja nicht mehr entwischt
     */
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
