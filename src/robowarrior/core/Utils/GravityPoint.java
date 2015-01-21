package robowarrior.core.Utils;

/**
 * Created by Jens on 21.01.2015.
 */
public class GravityPoint {
    public String name;
    public double x,y,power;
    public GravityPoint(String name,double x, double y ,double power){
        this.name=name;
        this.x=x;
        this.y=y;
        this.power=power;
    }

    public void update(double[] coord){
        x=coord[0];
        y=coord[1];
    }
}
