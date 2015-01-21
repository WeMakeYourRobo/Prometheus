package robowarrior.core.Utils;

import java.util.Random;

/**
 * Created by Jens on 21.01.2015.
 */
public class MapUtils {

    static public double[] randomPoint(double minX,double maxX,double minY,double maxY){
         Random r = new Random();
        double[] coords= new double[2] ;
        coords[0] = minX + (maxX - minX) * r.nextDouble();
        coords[1] = minY + (maxY - minY) * r.nextDouble();
        return coords;
    }

}
