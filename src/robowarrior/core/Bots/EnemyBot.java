package robowarrior.core.Bots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robowarrior.core.Utils.MathUtils;

import java.awt.geom.Point2D;

/**
 * @author Jens Laur, Markus Krabbenhöft, Dennis Pries
 * @version 1.0.2
 * Dient als Referenz zu unserem Gegner und hält seine Daten
 */
public class EnemyBot {


    private double bearing = 0;
    private double distance = 0;
    private double energy = 0;
    private double heading = 0;
    private String name = null;
    private double velocity = 0;
    private double[] coords = new double[2];

    public EnemyBot(ScannedRobotEvent e, AdvancedRobot me) {
        update(e, me);
    }

    public double getBearing() {
        return bearing;
    }

    public double getDistance() {
        return distance;
    }

    public double getEnergy() {
        return energy;
    }

    public double getHeading() {
        return heading;
    }


    /**
     * Position in n Sekunden
     * Gibt die Position des Gegener in n Sekunden zurück
     * @deprecated Nicht mehr benutzt weil GuessFactor Targeting besser ist
     */


    public double getFutureX(double time) {
        return coords[0] + Math.sin(Math.toRadians(getHeading())) * getVelocity() * time;
    }

    public double getFutureY(double time) {
        return coords[1] + Math.cos(Math.toRadians(getHeading())) * getVelocity() * time;
    }


    public double getX() {
        return coords[0];
    }

    public double getY() {
        return coords[1];
    }

    public double[] getCoords() {
        return coords;
    }

    public String getName() {
        return name;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setCoords(double[] coords) {
        this.coords = coords;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public void update(ScannedRobotEvent e, AdvancedRobot me) {
        bearing = e.getBearingRadians();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeadingRadians();
        name = e.getName();
        velocity = e.getVelocity();
        coords = MathUtils.getCoords(getBearing(), getDistance(), me.getHeadingRadians(), me.getX(), me.getY());

    }

}
