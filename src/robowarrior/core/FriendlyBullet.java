package robowarrior.core;

import robocode.AdvancedRobot;
import robowarrior.core.Bots.EnemyBot;

/**
 * Created by Jens on 05.03.2015.
 */
public class FriendlyBullet extends Bullet{


    public FriendlyBullet(double direction, double energy, double distance, double w, double h, double x, double y, double heading) {

        super(direction, energy,  distance,  w,  h,  x,  y,  heading);
    }
}
