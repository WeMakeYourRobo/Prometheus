package robowarrior;

import robocode.*;

import static robocode.util.Utils.*;

import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Utils.MathUtils; // doch brauch ich >-<

import java.util.ArrayList;

/**
 * Created by MKrabbenhoeft on 21.01.2015.
 */

public class NewMasterKILLERBOT extends AdvancedRobot {
    private ArrayList<EnemyBot> ListOfEnemey = new ArrayList<EnemyBot>();

    @Override
    public void run() {


    }

    @Override
    public void onBulletHit(BulletHitEvent event) {

    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {

    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {

    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {

    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {

    }

    @Override
    public void onHitRobot(HitRobotEvent event) {

    }

    @Override
    public void onHitWall(HitWallEvent event) {

    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {

    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // wenn wir wen gescannt haben, zielen und FIRE !
        // 1. Zielen
        //attack(event);
        // nur wenn noch nicht vorhanden
        if (this.ListOfEnemey.indexOf(event.getName()) < 0) {
            this.ListOfEnemey.add(new EnemyBot(event));
        }


    }

    @Override
    public void onWin(WinEvent event) {

    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {

    }

    @Override
    public void onStatus(StatusEvent e) {
        doMove();
        if (getRadarTurnRemaining() == 0) {
            setTurnRadarRight(360);
        }

        out.println(ListOfEnemey);
    }

    private void doMove() {
        setAhead(1);
    }

    private void attack(ScannedRobotEvent event) {
        double absoluteBearing = getHeading() + event.getBearing();
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
            scan();
        }
    }

    private boolean isInList(String robotname) {

        for (EnemyBot bot : ListOfEnemey) {
            if (bot.getName() == robotname) {
                return true;
            }

        }
        return false;
    }

}
