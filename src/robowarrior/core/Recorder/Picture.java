package robowarrior.core.Recorder;

/**
 * Created by Jens on 05.03.2015.
 */

// Hält die Daten den Gegners zu dem Zeitpunkt
public class Picture {



    double heading;



    double time;
    double velocity;
    double[] coords;

    public Picture(long time) {
    this.time=time;
        this.coords= new double[2];
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }
    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getX() {
        return coords[0];
    }

    public void setX(double x) {
        this.coords[0] = x;
    }

    public double getY() {
        return coords[1];
    }

    public void setY(double y) {
        this.coords[1] = y;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
