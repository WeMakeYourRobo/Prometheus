package robowarrior.core;

import robocode.AdvancedRobot;
import robocode.Robocode;
import robowarrior.core.Utils.MathUtils;

/**
 * Created by Jens on 23.01.2015.
 */
public class EnemyBullet {


    double direction=0;
    double energy=0;
    double speed=0;
    double firstdistance=0;
    double distance=0;
    double[] coords=new double[2];
    int i=1;

    public EnemyBullet(double direction, double energy, double distance, AdvancedRobot me) {
        this.direction = direction;
        this.energy = energy;
        this.speed = robocode.Rules.getBulletSpeed(energy);
        this.firstdistance = distance;
        this.coords= MathUtils.getCoords(direction,firstdistance,me.getHeading(),me.getX(),me.getY());
    }
    public void update(AdvancedRobot me){
        this.distance=firstdistance-(speed*i);
        i++;
        this.coords= MathUtils.getCoords(direction,firstdistance,me.getHeading(),me.getX(),me.getY());

    }


    public double getDirection() {
        return direction;
    }

    public double getEnergy() {
        return energy;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDistance() {
        return distance;
    }

    public double[] getCoords() {
        return coords;
    }
}
