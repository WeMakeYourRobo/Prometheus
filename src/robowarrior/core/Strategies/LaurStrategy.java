package robowarrior.core.Strategies;

import robocode.*;

import java.awt.*;

/**
 * Created by Jens on 16.01.2015.
 */
public class LaurStrategy  implements BaseStrategy {

    AdvancedRobot bot;

    public LaurStrategy(AdvancedRobot bot) {

        this.bot=bot;
    }
    //Steps through turning the gun by this interval.
    private static final double TURN_INTERVAL = 20.0;
    private ScannedRobotEvent currentEvent;
    private int dir=1;

    public void run() {
        this.bot.setAdjustRadarForGunTurn(false);
        bot.setColors(Color.BLACK,Color.RED,Color.RED,Color.BLACK,Color.BLACK);

    }

    public void doMove(ScannedRobotEvent event) {


        if (bot.getVelocity() == 0){
            dir *= -1;
        }

        bot.setTurnRight(event.getBearing() + 90);
        bot.setAhead(1000 * dir);
        bot.setTurnRight(event.getBearing() + 90);


        if (bot.getTime() %  15== 0) {
            dir *= -1;
            bot.setAhead(150 * dir);
        }
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        this.currentEvent = event;
        doMove(event);
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
    public void onWin(WinEvent event) {

    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {

    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {

    }

    @Override
    public void onStatus(StatusEvent event) {
        System.out.print("aa");
        //Scan for a robot.
        if(this.currentEvent == null) {
            this.bot.turnGunRight(TURN_INTERVAL);
        }

        //If a robot is found, track it.
        else {
            double absoluteHeading = this.bot.getHeading() + currentEvent.getBearing();
            //Use the law of cosines to approximate the new distance after the enemy moves.
            double predictedAngle = 360.0 - currentEvent.getHeading() - (180 - absoluteHeading);
            double predictedDist = Math.pow(currentEvent.getDistance(), 2) + Math.pow(currentEvent.getVelocity(), 2) -
                    (2 * currentEvent.getDistance() * currentEvent.getVelocity() * Math.cos(predictedAngle));

            //Use the law of sines to approximate the new absolute heading
            double turnAngle = Math.asin((currentEvent.getVelocity() / predictedDist) * Math.sin(predictedAngle));

            //With this information, turn the gun to find the enemy
            this.bot.turnGunRight(absoluteHeading + turnAngle - this.bot.getGunHeading());
            bot.setFire(10);
            //Reset the current event and find the enemy again.
            this.currentEvent = null;
            this.bot.scan();
        }
    }
}
