package robowarrior.core.Bots;

import robocode.AdvancedRobot;
import robocode.Robot;


import robocode.ScannedRobotEvent;
import robowarrior.core.Utils.MathUtils;
// Speichert EnemyProps und updated sie
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


    // Position in n sec
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

    public void update(ScannedRobotEvent e, AdvancedRobot me) {
        bearing = e.getBearing();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeading();
        name = e.getName();
        velocity = e.getVelocity();
        coords = MathUtils.getCoords(getBearing(), getDistance(), me.getHeading(), me.getX(), me.getY());

    }

}
