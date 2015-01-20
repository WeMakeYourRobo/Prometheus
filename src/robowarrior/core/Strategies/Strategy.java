package robowarrior.core.Strategies;

import robocode.*;

import java.awt.*;

/**
 * Created by Jens on 16.01.2015.
 */
public class Strategy implements BaseStrategy {

    AdvancedRobot bot;

    public Strategy(AdvancedRobot robot) {
        bot=robot;
    }

    @Override
    public void run() {
        bot.setColors(Color.BLACK,Color.RED,Color.RED,Color.BLACK,Color.BLACK);
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {

    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {

    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {

    }

    @Override
    public void onDeath(DeathEvent event) {

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

    }

    @Override
    public void onWin(WinEvent event) {

    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {

    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {

    }
}
