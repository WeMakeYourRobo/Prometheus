package robowarrior.core.Bots;

import robocode.Robot;


import robocode.ScannedRobotEvent;
import robowarrior.core.Utils.MathUtils;

public class EnemyBot {
    private double bearing = 0;
    private double distance = 0;
    private double energy = 0;
    private double heading = 0;
    private String name = null;
    private double velocity = 0;
    private double[] coords=null;

    public EnemyBot() {
        reset();
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
    public double[] getCoords() {
        return coords;
    }

    public String getName() {
        return name;
    }

    public double getVelocity() {
        return velocity;
    }

    public void update(ScannedRobotEvent e) {
        bearing = e.getBearing();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeading();
        name = e.getName();
        velocity = e.getVelocity();

    }

    public void reset() {
        bearing = 0;
        distance = 0;
        energy = 0;
        heading = 0;
        name = "";
        velocity = 0;
        coords=null;
    }

    public boolean none() {
        return true;
    }
}
