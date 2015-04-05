package robowarrior.core;

import robocode.AdvancedRobot;

/**
 * @author Jens Laur, Markus Krabbenhöft, Dennis Pries
 * @version 1.0.0
 * Wrapper Klasse um zwischen Bullets vom Enemy und von uns unterscheiden zu können
 */
public class EnemyBullet extends Bullet{


    public EnemyBullet(double direction, double energy, double distance, double w, double h, double x, double y, double heading) {

        super(direction, energy,  distance,  w,  h,  x,  y,  heading);
    }
}
