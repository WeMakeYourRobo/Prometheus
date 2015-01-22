package robowarrior;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import robowarrior.core.Bots.EnemyBot;

import java.util.ArrayList;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class BulletAvoidBot extends AdvancedRobot {
    int movementDirection = 1;
    private EnemyBot Opfer = null;

    public void run() {
        setAdjustRadarForGunTurn(false);
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {

    }

    public void onScannedRobot(ScannedRobotEvent event) {
        setTurnRight(event.getBearing() + 90);
        double changeInEnergy=0;
        if (Opfer != null) {
            changeInEnergy = Opfer.getEnergy() - event.getEnergy();
            Opfer.update(event);
        } else {
            Opfer = new EnemyBot(event);

        }


        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            movementDirection = -movementDirection;
            setAhead((event.getDistance() / 4 + 25) * movementDirection);
        }

    }

    @Override
    public void onStatus(StatusEvent e) {
        if (getRadarTurnRemaining() == 0) {
            setTurnRadarRight(360);
        }
        attack();
    }

    private void attack() {
        if (Opfer != null) {
            double absoluteBearing = getHeading() + Opfer.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());


            if (Math.abs(bearingFromGun) <= 3) {
                setTurnGunRight(bearingFromGun);

                if (getGunHeat() == 0) {
                    setFire(10);
                }
            } else {
                setTurnGunRight(bearingFromGun);
            }

            if (bearingFromGun == 0) {
            }


        }
    }
}
