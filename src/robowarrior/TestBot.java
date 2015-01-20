package robowarrior;

import robocode.*;
import robowarrior.core.Bots.BaseRobot;
import robowarrior.core.Strategies.Strategy;

/**
 * Created by Jens on 16.01.2015.
 */
public class TestBot  extends BaseRobot{
    public void run() {
        Strategy currentStrategy=new Strategy(this);
        this.setCurrentStrategy(currentStrategy);
        super.run();
    }


}
