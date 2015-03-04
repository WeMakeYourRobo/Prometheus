package robowarrior;

import robocode.*;
import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Utils.MathUtils;

import java.awt.*;
import java.util.ArrayList;

import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * Created by MKrabbenhoeft on 21.01.2015.
 */

public class Predictor extends AdvancedRobot {
    private ArrayList<EnemyBot> ListOfEnemey = new ArrayList<EnemyBot>();
    private EnemyBot Opfer = null;

    @Override
    public void run() {
        setTurnRadarRight(360);
        setColors(Color.BLACK,Color.PINK,Color.PINK,Color.PINK,Color.PINK);
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
        if(event.getName()==this.Opfer.getName()){
            this.Opfer=null;
        }
        this.ListOfEnemey.remove( this.ListOfEnemey.indexOf( getRobotByName(event.getName())));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        // wenn wir wen gescannt haben, zielen und FIRE !
        // 1. Zielen
        //attack(event);
        // nur wenn noch nicht vorhanden
        if ( !this.isInList(event.getName())) {
            this.ListOfEnemey.add(new EnemyBot(event,this));
        }
        else
        {
         ListOfEnemey.get(ListOfEnemey.indexOf(getRobotByName(event.getName()))).update(event,this);
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
        //

            for (EnemyBot bot : ListOfEnemey) {
                {

                    this.Opfer=bot;

                    System.out.println(MathUtils.getFuturePosition(getX(), getY(), getHeading(), this.Opfer));
                }

            }

            attack();
        }

   /*     for (EnemyBot bot : ListOfEnemey) {
          System.out.print(bot.getName()+" |");

        }
        System.out.print("\n");
*/
    }

    private void doMove() {
        setAhead(100);

    }

    private void attack() {
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
        setTurnRadarRight(360);

    }


    private boolean isInList(String robotname) {

        for (EnemyBot bot : ListOfEnemey) {
            if (bot.getName() == robotname) {
                return true;
            }

        }
        return false;
    }

    private EnemyBot getRobotByName(String robotname) {
        for (EnemyBot bot : ListOfEnemey) {
            if (bot.getName() == robotname) {
                return bot;
            }

        }
        return null;
    }



}
