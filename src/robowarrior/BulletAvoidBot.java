package robowarrior;

import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import robocode.*;
import robowarrior.core.Bots.EnemyBot;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import static robowarrior.core.Utils.MapUtils.getRigthWinkel;
import static robowarrior.core.Utils.MathUtils.getCoords;

public class BulletAvoidBot extends AdvancedRobot {
    int movementDirection = 1;
    double wallMargin=60;
    private EnemyBot Opfer = null;
    private Rectangle2D rect =new Rectangle2D.Double(0,0,100,100);

    public void run() {
        setAdjustRadarForGunTurn(false);
        rect.setRect(10,10,getBattleFieldWidth()-10,getBattleFieldHeight()-10);

        Condition wallHit=new Condition("near_wall") {
            public boolean test() {
                return (

                        (getX() <= wallMargin ||
                                // or we're too close to the right wall
                                getX() >= getBattleFieldWidth() - wallMargin ||
                                // or we're too close to the bottom wall
                                getY() <= wallMargin ||
                                // or we're too close to the top wall
                                getY() >= getBattleFieldHeight() - wallMargin)
                );
            }
        };

        wallHit.setPriority(99);
        addCustomEvent(wallHit);

        Condition enemyHit=new Condition("near_enemy") {
            public boolean test() {
                return (
                    Opfer.getDistance()<=250

                );
            }
        };

        wallHit.setPriority(99);
        addCustomEvent(wallHit);
    }

    @Override
    public void onCustomEvent(CustomEvent event) {
        if (event.getCondition().getName().equals("near_wall")) {

            out.println("event fired");
            goTo(new Point2D.Double(getBattleFieldWidth()/2,getBattleFieldHeight()/2));
        }
        if (event.getCondition().getName().equals("near_enemy")) {

            out.println("near enemy fired");
            goTo(new Point2D.Double(getBattleFieldWidth()/2,getBattleFieldHeight()/2));
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {

    }

    public void onScannedRobot(ScannedRobotEvent event) {
        setTurnRight(event.getBearing() + 90);
        double changeInEnergy=0;
        if (Opfer != null) {
            changeInEnergy = Opfer.getEnergy() - event.getEnergy();
            Opfer.update(event);
        } else {
            Opfer = new EnemyBot(event);

        }

        if (changeInEnergy > 0 && changeInEnergy <= 3) {

                setAhead((event.getDistance() / 4 + 25) * movementDirection);

        }
    }

    @Override
    public void onStatus(StatusEvent e) {

        if (getRadarTurnRemaining() == 0) {
            setTurnRadarRight(360);
        }
        attack();
    }

    private void attack() {
        if (Opfer != null) {
            double absoluteBearing = getHeading() + Opfer.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());


            if (Math.abs(bearingFromGun) <= 3) {
                setTurnGunRight(bearingFromGun);

                if (getGunHeat() == 0) {
                    double firePower=0;
                    double maxDistance=Math.pow(getBattleFieldWidth(),2)+Math.pow(getBattleFieldHeight(),2);
                    if(Opfer.getDistance()<50){
                        firePower=3.0;
                    }

                    setFire(firePower);
                }
            } else {
                setTurnGunRight(bearingFromGun);
            }

            if (bearingFromGun == 0) {
            }


        }
    }

    private void goTo( Point2D NextPoint) {
        // Methode damit ich mich zu meinen Nächsten Punkt fahre....

        // Distance & Winkel holen
        double distance = this.location().distance(NextPoint);
        double angle = normalRelativeAngle(absoluteBearing(location(), NextPoint) - getHeading());
        // Distance und Winkel anpassen
        double[] aangle = getRigthWinkel(angle, distance);

        // Drehen und fahren !
        setTurnRight(aangle[0]);
        setAhead(aangle[1]);
    }

    private Point2D location() {
        // Punkt erstellen mit meinen koordinaten & zurück geben
        return new Point2D.Double(getX(), getY());
    }

    private double absoluteBearing(Point2D source, Point2D target) {
        // WInkelerrechnen ( in Bogenmaß) , mithilfe der Tan funktionen, aus minen Punkt und miene Wunsch punk
        return Math.toDegrees(Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()));
    }

    private double normalRelativeAngle(double angle) {
        // winkel ...
        // mit den Parameter,
        // modullieren...
        //  und irgendwas irgendwie mit den Winkeln machen ... ? O.o

        double relativeAngle = angle % 360;
        if (relativeAngle <= -180)
            return 180 + (relativeAngle % 180);
        else if (relativeAngle > 180)
            return -180 + (relativeAngle % 180);
        else
            return relativeAngle;
    }

}
