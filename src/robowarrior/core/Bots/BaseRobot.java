package robowarrior.core.Bots;

import robocode.*;
import robowarrior.core.Strategies.BaseStrategy;
import robowarrior.core.Strategies.Strategy;

/**
 * Created by Jens on 16.01.2015.
 */
public class BaseRobot extends AdvancedRobot {
    public BaseStrategy currentStrategy;

    public void setCurrentStrategy(BaseStrategy s){
        this.currentStrategy=s;
    }

    @Override
    public void run() {
        this.currentStrategy.run();
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        this.currentStrategy.onBulletHit(event);
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {
        this.currentStrategy.onBulletHitBullet(event);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        this.currentStrategy.onBulletMissed(event);
    }

    @Override
    public void onDeath(DeathEvent event) {
        this.currentStrategy.onDeath(event);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        this.currentStrategy.onHitByBullet(event);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        this.currentStrategy.onHitRobot(event);
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        this.currentStrategy.onHitWall(event);
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        this.currentStrategy.onRobotDeath(event);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        this.currentStrategy.onScannedRobot(event);
    }

    @Override
    public void onWin(WinEvent event) {
        this.currentStrategy.onWin(event);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        this.currentStrategy.onRoundEnded(event);
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        this.currentStrategy.onBattleEnded(event);
    }

    @Override
    public void onStatus(StatusEvent e) {
        this.currentStrategy.onStatus(e);
    }
}
