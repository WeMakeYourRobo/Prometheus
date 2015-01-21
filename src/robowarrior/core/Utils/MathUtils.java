package robowarrior.core.Utils;

/**
 * Created by Jens on 16.01.2015.
 */
public class MathUtils {

        static public double[] getCoords(double enemyBearing,double enemyDistance,double selfHeading,double selfX,double selfY) {
        double angleToEnemy = enemyBearing;
        // Calculate the angle to the scanned robot
        double angle = Math.toRadians((selfHeading + angleToEnemy % 360));

        // Calculate the coordinates of the robot

        double[] coord=null;
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


}
