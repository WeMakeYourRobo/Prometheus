package robowarrior;

import robocode.*;
import robowarrior.core.Bots.EnemyBot;
import robowarrior.core.EnemyBullet;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * Created by Jens on 22.01.2015.
 */
public class LordGalvatron extends AdvancedRobot{

    private boolean initComplete=false;
    private boolean handlingHitWallEvent=false;


    private HashMap enemyBots=new HashMap();
    private HashMap <String, EnemyBullet> enemyBullets=new HashMap<String, EnemyBullet>();
    private EnemyBot enemy=null;
    private int direction=1;
    private Rectangle2D bounds=new Rectangle2D.Double(0,0,0,0);
    private Polygon walls=null;
    private double battleField[]=new double[2];

    @Override
    public void run() {



        initComplete=true;
        battleField[0]=getBattleFieldWidth();
        battleField[1]=getBattleFieldHeight();
        bounds.setRect(0,0,battleField[0],battleField[1]);

        int[] xPoints=new int[]{50
                ,50
                ,100
                ,(int) battleField[0]-100
                ,(int) battleField[0]-50
                ,(int) battleField[0]-50
                ,(int) battleField[0]-100
                ,100};
        int[] yPoints=new int[]{100
                ,(int) battleField[1]-100
                ,(int) battleField[1]-50
                ,(int) battleField[1]-50
                ,(int) battleField[1]-100
                ,100
                ,50
                ,50};
        walls=new Polygon(xPoints,yPoints,8);
        initCustomEvents();
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
    public void onCustomEvent(CustomEvent event){

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
        if(enemyBots.containsKey(event.getName())){
            enemyBots.remove(event.getName());
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        double energyDifference=0;
        if(enemy!=null) {
            double actualEnergy = event.getEnergy();
             energyDifference=enemy.getEnergy()-actualEnergy;
        }

        if(!enemyBots.containsKey(event.getName())){
            enemyBots.put(event.getName(),new EnemyBot(event,this));
        } else {
            EnemyBot bot=(EnemyBot) enemyBots.get(event.getName());
            bot.update(event, this);
        }
        attack();
        if(enemy!=null){
           out.println("has enemy");

            if (energyDifference > 0 && energyDifference <= 3) {
                enemyBullets.put("bullet"+event.getBearing()+getTime(),new EnemyBullet(event.getBearing(),energyDifference, event.getDistance(), this));
                double[] coord=enemyBullets.get("bullet"+event.getBearing()+getTime()).getCoords();
                out.println(coord[0]+" "+coord[1]);
            }
        }


    }

    public void attack(){
        enemy=getNearestBot();
        double absoluteBearing = getHeading() + enemy.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
        double firePower = Math.min(400 / enemy.getDistance(), 3);

        double timeToHit=enemy.getDistance()/firePower;
        double enemyTravelDistance=enemy.getVelocity()*timeToHit;

        if (Math.abs(bearingFromGun) <= 13) {
            setTurnGunRight(bearingFromGun);

            if (getGunHeat() == 0) {
                setFire(10);
            }
        } else {
            setTurnGunRight(bearingFromGun);
        }


    }
    public EnemyBot getNearestBot(){
        double distance=9999999;
        EnemyBot currBot=null;
        for (Object obj : enemyBots.values()) {
            EnemyBot bot=(EnemyBot) obj;

            if (bot.getDistance() < distance) {
                distance = bot.getDistance();
                currBot = bot;
            }
        }
        return currBot;
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        
    }

    @Override
    public void onPaint(Graphics2D g) {
                g.setColor(Color.blue);
                g.drawPolygon(walls);
        for(Map.Entry<String, EnemyBullet> entry : enemyBullets.entrySet()) {

            EnemyBullet bullet = entry.getValue();

            double[]coord =bullet.getCoords();
            int x=(int)coord[0];
            int y=(int)coord[1];
            out.print(x+"/"+y+"||");
            g.drawRect(x-5,y-5,10,10);

        }
    }

    @Override
    public void onStatus(StatusEvent e) {
        // alles detachen bitte

        setAdjustGunForRobotTurn(false);
        setAdjustRadarForRobotTurn(false);
        setAdjustRadarForGunTurn(false);
        setTurnRadarLeft(360);

        //Update alle Bullets
        for(Map.Entry<String, EnemyBullet> entry : enemyBullets.entrySet()) {
            String key = entry.getKey();
            EnemyBullet bullet = entry.getValue();

            bullet.update(this);
            double[]coord =bullet.getCoords();
            int x=(int)coord[0];
            int y=(int)coord[1];
            out.print(x+"/"+y+"||");


        }


    }

    @Override
    public void onSkippedTurn(SkippedTurnEvent event) {
        
    }

    public void initCustomEvents(){

    }
}
