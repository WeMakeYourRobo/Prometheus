package robowarrior.core.Strategies;

import robocode.*;

/**
 * Created by Jens on 16.01.2015.
 */
public interface BaseStrategy{

    public void run();
    public void onBulletHit(BulletHitEvent event);
    public void onBulletHitBullet(BulletHitBulletEvent event);
    public void onBulletMissed(BulletMissedEvent event);
    public void onDeath(DeathEvent event);
    public void onHitByBullet(HitByBulletEvent event);
    public void onHitRobot(HitRobotEvent event);
    public void onHitWall(HitWallEvent event);
    public void onRobotDeath(RobotDeathEvent event);
    public void onScannedRobot(ScannedRobotEvent event);
    public void onWin(WinEvent event);
    public void onRoundEnded(RoundEndedEvent event);
    public void onBattleEnded(BattleEndedEvent event);
    public void onStatus(StatusEvent event);
}
