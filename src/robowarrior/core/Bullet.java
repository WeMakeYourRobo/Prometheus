package robowarrior.core;

import robocode.*;
import robowarrior.core.Utils.MathUtils;

/**
 * Created by Jens on 23.01.2015.
 */
// Speichert Bullet-Eigenschaften und trackt sie mit, so dass die Position jedes Bullets zu jeder Zeit bekannt ist
public class Bullet {


    double direction=0;
    double energy=0;
    double speed=0;
    double firstdistance=0;
    double distance=0;
    double myHead=0;
    double[] coords=new double[2];
    double[] myCoords=new double[2];
    int i=2;
    public boolean selfdestroy=false;
    double width;
    double height;

    public Bullet(double direction, double energy, double distance, double w, double h, double x, double y, double heading) {
        this.direction = direction;
        this.energy = energy;
        this.speed = robocode.Rules.getBulletSpeed(energy);
        this.firstdistance = distance;
        this.coords= MathUtils.getCoords(direction,firstdistance,heading,x,y);
        // Unsere Daten speichern, um sie die Position des Bullets neu berechnen zu können
        this.myCoords[0]=x;
        this.myCoords[1]=y;
        this.myHead=heading;
        this.width=w;
        this.height=h;



    }
    public void update(){
        this.distance=firstdistance-(speed*i);
        i++;
        this.coords= MathUtils.getCoords(direction,distance,myHead,myCoords[0],myCoords[1]);
        // Lösche Flag setzen wenn es ausserhalb des Feldes ist
        if(coords[0] < 0 || coords[1] < 0 || coords[0] > width || coords[1] > height) {
            selfdestroy = true;
        }


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
