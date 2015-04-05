package robowarrior.core.Utils;


import robocode.util.Utils;

import java.awt.geom.Point2D;

/**
 * Created by Jens on 16.01.2015.
 */
public class MathUtils {


    static public double bulletSpeed(double power) {
        return 20 - 3 * power;
    }

    static public double maxEscapeAngle(double power) {
        return Math.asin(8 / bulletSpeed(power));
    }
    // Gibt das Vorzeichen einer Zahl zurück
    static public int sign(double v) {
        return v < 0 ? -1 : 1;
    }

    // Get Coords of Enemy
    static public double[] getCoords(double enemyBearing, double enemyDistance, double selfHeading, double selfX, double selfY) {
        double angleToEnemy = enemyBearing;
        double angle = selfHeading + angleToEnemy ;
        double[] coord = new double[2];
        coord[0] = (selfX + Math.sin((angle)) * enemyDistance);
        coord[1] = (selfY + Math.cos((angle)) * enemyDistance);
        return coord;
    }
       // Gibt Coordinate aufgrund des Ursprungspunktes ,des Winkels zur Coordinate und der Distanz zurück
    static public Point2D project(Point2D sourceLocation, double angle, double length) {
        return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
                sourceLocation.getY() + Math.cos(angle) * length);
    }


    static public int minMax(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    static public double absoluteBearing(Point2D source, Point2D target) {
        // Winkel zwischen 2 Punkten
        return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
    }

    static public double normalRelativeAngle(double angle) {
      return Utils.normalRelativeAngle(angle);
    }

}
