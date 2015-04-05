package robowarrior.core;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.util.Utils;
import robowarrior.core.Utils.MathUtils;

import java.awt.geom.Point2D;

/**
 * @author Jens Laur, Markus Krabbenhöft, Dennis Pries
 * @version 1.0.0
 * Representiert eine Welle
 */
public class Wave extends Condition{

    public double gunX;
    public double gunY;
    public double enemyBearing;
    public double power;
    public long time;

    public int direction;
    public int[] segment;

    // Variablen die bei jedem onScanned Event geupdated werden
    //Aktuelle Zeit
    public static long now;
    // Eine Referenz auf unseren Roboter
    public static AdvancedRobot me;
    // Aktuelle Positin des Gegners
    public static double enemyX;
    public static double enemyY;

    /**
     *
     * @param x X-Position von uns als die Welle erstellt wurde
     * @param y Y-Position von uns als die Welle erstellt wurde
     * @param vbearing absoluter Winkel zum Gegner
     * @param vpower Power der Welle wird zur Geschwindigkeitsberechnung gebraucht
     * @param vdirection Richtung in die der Gegner fährt 1 wenn vorwärts -1 wenn rückwärts
     * @param vtime Zeit zu der die Welle erstellt wurde
     * @param vsegment Referenz auf die stats Variable des Roboters
     */
    public Wave(double x, double y, double vbearing, double vpower, int vdirection, long vtime, int[] vsegment){
        gunX = x;
        gunY = y;
        enemyBearing = vbearing;
        this.power = vpower;
        this.direction = vdirection;
        time = vtime;
        segment = vsegment;
    }

    /**
     *
     * Wird von robocode jeden Tick ausgeführt und löscht sich selbst wenn die Welle den Gegner erreicht hat bzw. hinter ihm ist
     *
     */
    public boolean test(){
        //Hat das Bullet den Gegner erreicht
        if (Point2D.distance(gunX, gunY, enemyX, enemyY) <= (now - time) * MathUtils.bulletSpeed(power)){
            // Winkel von Gun zum Gegner
            double desiredDirection = Math.atan2(enemyX - gunX, enemyY - gunY);
            //Offset von gegner winkel und gun winkel
            double angleOffset = Utils.normalRelativeAngle(desiredDirection - enemyBearing);
            //Guess Factor berechnen
            // MinMax damit wir bei einer Zahl zwischen 1 und -1 bleiben ansonsten gleiche Formel wie http://robowiki.net/wiki/GuessFactor#Calculation
            double guessFactor = Math.max(-1, Math.min(1, angleOffset / MathUtils.maxEscapeAngle(power))) * direction;
            int index = (int) Math.round((segment.length - 1) / 2 * (guessFactor + 1));
            // Den Zähler für diesen Index einen hoch setzen & somit den Statistik ebenfalls ein hochsetzen ( pass bei reference )
            segment[index]++;
            me.removeCustomEvent(this);
        }
        return false;
    }
}


