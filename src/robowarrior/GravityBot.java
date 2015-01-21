package robowarrior;

import robocode.*;

import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.Utils.GravityPoint;
import robowarrior.core.Utils.MapUtils;
import robowarrior.core.Utils.MathUtils;

import java.util.ArrayList;

/**
 * Created by Jens on 21.01.2015.
 */
public class GravityBot extends AdvancedRobot {
    private ArrayList<EnemyBot> ListOfEnemey = new ArrayList<EnemyBot>();
    ArrayList<GravityPoint> points=new ArrayList<GravityPoint>();


    @Override
    public void run() {
        double battlefieldWidth=getBattleFieldWidth();
        double battlefieldHeight=getBattleFieldHeight();
        for(int i=0;i<=10;i++) {
            double[] coords= MapUtils.randomPoint(0,0,battlefieldWidth,battlefieldHeight);
            points.add(new GravityPoint("yolo",coords[0],coords[1],i*100));
        }

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
        this.ListOfEnemey.remove( this.ListOfEnemey.indexOf( getRobotByName(event.getName())));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        double[] coords=MathUtils.getCoords(event.getBearing(),event.getDistance(),getHeading(),getX(),getY());
        if ( !this.isInList(event.getName())) {
            this.ListOfEnemey.add(new EnemyBot(event));

            points.add(new GravityPoint(event.getName(),coords[0],coords[1],10));
        }
        else
        {

            points.get(points.indexOf(getPointByName(event.getName()))).update(coords);
        }
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
    public void onStatus(StatusEvent e) {
        double xforce=0;
        double yforce=0;
        double ang;
        for(GravityPoint p:points){
            double force=p.power/Math.pow(MathUtils.getDistance(getX(), getY(), p.x, p.y), 2);
            ang =MathUtils.normaliseBearing(Math.PI/2 - Math.atan2(getY() - p.y, getX() - p.x));
            //Add the components of this force to the total force in their
            //respective directions
            xforce += Math.sin(ang) * force;
            yforce += Math.cos(ang) * force;
        }
        goTo(getX()-xforce,getY()-yforce);

    }
    void goTo(double x, double y) {
        double dist = 20;
        double angle = Math.toDegrees(getHeading()+MathUtils.getAngleToPoint(getX(),getY(),x,y));
        double r = turnTo(angle);
        setAhead(dist * r);
    }
    int turnTo(double angle) {
        double ang;
        int dir;
        ang = MathUtils.normaliseBearing(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            dir = -1;
        }
        else if (ang < -90) {
            ang += 180;
            dir = -1;
        }
        else {
            dir = 1;
        }
        setTurnLeft(ang);
        return dir;
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
    private GravityPoint getPointByName(String name) {
        for (GravityPoint bot : points) {
            if (bot.name == name) {
                return bot;
            }

        }
        return null;
    }
}
