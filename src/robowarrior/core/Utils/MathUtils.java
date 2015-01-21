package robowarrior.core.Utils;

import robowarrior.core.Bots.EnemyBot;

/**
 * Created by Jens on 16.01.2015.
 */
public class MathUtils {

        static public double[] getCoords(double enemyBearing,double enemyDistance,double selfHeading,double selfX,double selfY) {
        double angleToEnemy = enemyBearing;
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((selfHeading + angleToEnemy % 360));

        // Calculate the coordinates of the robot

        double[] coord= new double[2];
        coord[0] = (selfX + Math.sin(angle) * enemyDistance);
        coord[1] = (selfY + Math.cos(angle) * enemyDistance);
        return coord;
    }

    static public  double getAngleToPoint(double x, double y,double destX,double destY) {
        //Get heading to the center.
        double distX =  destX - x;
        double distY = destY - y;

        double angle = Math.atan(distX / distY) * (180.0/Math.PI);

        //Check if the robot needs to move south instead of north.
        if(distY < 0) {
            angle += 180.0;
        }

        return angle;
    }
    static public double getDistance(double x1,double y1, double x2,double y2) {
        double x = x2-x1;
        double y = y2-y1;
        double range = Math.sqrt(x*x + y*y);
        return range;
    }

    static public double[] getFuturePosition(double x,double y, double heading, EnemyBot bot){
        double[] coords=null;


        return coords;
    }
   static public double normaliseBearing(double ang) {
        if (ang > Math.PI)
            ang -= 2*Math.PI;
        if (ang < -Math.PI)
            ang += 2*Math.PI;
        return ang;
    }

}
