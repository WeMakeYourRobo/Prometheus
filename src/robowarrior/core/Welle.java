package robowarrior.core;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.util.Utils;
import robowarrior.core.Utils.MathUtils;

import java.awt.geom.Point2D;

/**
 * Created by Jens on 31.03.2015.
 */
public class Welle extends Condition {

    public static Point2D targetLocation;

    public double bulletPower;
    public Point2D gunLocation;
    public double bearing;
    public double lateralDirection;

    private static final double MAX_DISTANCE = 1000;
    private static final int DISTANCE_INDEXES = 40;
    private static final int VELOCITY_INDEXES = 40;
    private static final int BINS = 31;
    private static final int MIDDLE_BIN = 15;
    private double MAX_ESCAPE_ANGLE = Math.asin(8 / MathUtils.bulletSpeed(this.bulletPower));
    private double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double)MIDDLE_BIN;

    private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];

    private int[] buffer;
    private AdvancedRobot robot;
    private double distanceTraveled;

    public Welle(AdvancedRobot _robot) {
        this.robot = _robot;
    }

    public boolean test() {
        advance();
        //Wenn welle am Ziel ist darf sie sterben gehen
        if (hasArrived()) {
            buffer[currentBin()]++;
            robot.removeCustomEvent(this);
        }
        return false;
    }

    public double mostVisitedBearingOffset() {
        return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
    }

    public void setSegmentations(double distance, double velocity, double lastVelocity) {
        int distanceIndex = (int)(distance / (MAX_DISTANCE / DISTANCE_INDEXES));
        int velocityIndex = (int)Math.abs(velocity / 2);
        int lastVelocityIndex = (int)Math.abs(lastVelocity / 2);
        buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
    }

    private void advance() {
        // Die Welle weiter laufen lassen
        distanceTraveled += MathUtils.bulletSpeed(bulletPower);
    }

    private boolean hasArrived() {
        // Die Welle ist angekommen
        return distanceTraveled > gunLocation.distance(targetLocation) - 18;
    }

    private int currentBin() {
        int bin = (int)Math.round(((MathUtils.normalRelativeAngle(MathUtils.absoluteBearing(gunLocation, targetLocation) - bearing)) /
                (lateralDirection * BIN_WIDTH)) + MIDDLE_BIN);
        return MathUtils.minMax(bin, 0, BINS - 1);
    }

    private int mostVisitedBin() {
        int mostVisited = MIDDLE_BIN;
        for (int i = 0; i < BINS; i++) {
            if (buffer[i] > buffer[mostVisited]) {
                mostVisited = i;
            }
        }
        System.out.println(buffer[mostVisited]);
        return mostVisited;
    }
}

