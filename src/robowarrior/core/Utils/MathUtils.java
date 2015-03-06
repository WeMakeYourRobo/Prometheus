package robowarrior.core.Utils;


import java.awt.geom.Point2D;

/**
 * Created by Jens on 16.01.2015.
 */
public class MathUtils {

    static public double[] getCoords(double enemyBearing, double enemyDistance, double selfHeading, double selfX, double selfY) {
        double angleToEnemy = enemyBearing;
        double angle = Math.toRadians((selfHeading + angleToEnemy % 360));
        double[] coord = new double[2];
        coord[0] = (selfX + Math.sin((angle)) * enemyDistance);
        coord[1] = (selfY + Math.cos((angle)) * enemyDistance);
        return coord;
    }

    static public double getAngleToPoint(double x, double y, double destX, double destY) {
        //Get heading to the center.
        double distX = destX - x;
        double distY = destY - y;

        double angle = Math.atan(distX / distY) * (180.0 / Math.PI);

        //Check if the robot needs to move south instead of north.
        if (distY < 0) {
            angle += 180.0;
        }

        return angle;
    }

    static public double getDistance(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        double range = Math.sqrt(x * x + y * y);
        return range;
    }


    static public double normaliseBearing(double ang) {
        if (ang > Math.PI)
            ang -= 2 * Math.PI;
        if (ang < -Math.PI)
            ang += 2 * Math.PI;
        return ang;
    }

    static public double absoluteBearing(Point2D source, Point2D target) {
        // WInkelerrechnen ( in Bogenmaß) , mithilfe der Tan funktionen, aus minen Punkt und miene Wunsch punk
        return Math.toDegrees(Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()));
    }

    static public double normalRelativeAngle(double angle) {
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
