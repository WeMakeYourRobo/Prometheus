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

}
