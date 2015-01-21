package robowarrior.core.Utils;

import java.util.Random;

/**
 * Created by Jens on 21.01.2015.
 */
public class MapUtils {

    static public double[] randomPoint(double minX,double maxX,double minY,double maxY){

        double[] coords= new double[2] ;
        coords[0] = Math.random()*maxX+1;
        coords[1] =  Math.random()*maxY+1;
        return coords;
    }

    static public double[] getRigthWinkel(double angle, double distance ) {
        double[] result=new double[2];
        if (Math.abs(angle) > 90.0) {
            // wenn der Winkel > 90 Grad, dann fahren wir rückwärts
            distance *= -1.0;
            // damit der Turnright funktioniert, verändern wir den winkel ... ?
            // und ggf. auch negativ ? o.o
            if (angle > 0.0) {
                angle -= 180.0;
            }
            else {
                angle += 180.0;
            }
        }

        result[0] =angle;
        result[1] = distance;
        return result;
    }
}
