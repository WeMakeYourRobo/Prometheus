package robowarrior;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
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
    private EnemyBot Opfer = null;
    private Rectangle2D rect =new Rectangle2D.Double(0,0,100,100);

    public void run() {
        setAdjustRadarForGunTurn(false);
        rect.setRect(10,10,getBattleFieldWidth()-10,getBattleFieldHeight()-10);
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
            if(!rect.contains(getX(),getY())){
                out.println(getX()+" | "+getY());
                movementDirection = -movementDirection; nn

            }
            movementDirection = -movementDirection;

            double Dis = (event.getDistance() / 4 + 25) * movementDirection;

           double[] pos = getCoords(0,Dis,this.getHeading(), getX(), getY());

            if(!rect.contains(pos[0],pos[1])){
               goTo(new Point2D.Double(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2)) ;
            }
            else {
                setAhead((event.getDistance() / 4 + 25) * movementDirection);
            }

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
                    setFire(.1);
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
