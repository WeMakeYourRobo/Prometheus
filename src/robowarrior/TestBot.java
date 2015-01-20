package robowarrior;

import robocode.*;
import robowarrior.core.Bots.BaseRobot;
import robowarrior.core.Strategies.LaurStrategy;
import robowarrior.core.Strategies.StandingStrategy;
import robowarrior.core.Strategies.Strategy;

/**
 * Created by Jens on 16.01.2015.
 */
public class TestBot  extends BaseRobot{
    @Override
    public void run() {

        this.setCurrentStrategy(new LaurStrategy(this));
        super.run();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        if(getEnergy()<=50){
            this.setCurrentStrategy(new StandingStrategy(this));

        }
        super.onHitByBullet(event);
    }


}
