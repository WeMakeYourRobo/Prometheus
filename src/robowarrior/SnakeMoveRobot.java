package robowarrior;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.StatusEvent;
import robowarrior.core.Utils.MathUtils;

import java.awt.geom.*;

import static robowarrior.core.Utils.MapUtils.getRigthWinkel;

/**
 * Created by MKrabbenhoeft on 21.01.2015.
 */
public class SnakeMoveRobot extends AdvancedRobot {
    private Point2D NextPoint = null;

    @Override
    public void run() {
        // Do Something <_<
        // Mach hier irgendwie nichts schönes .....
        System.out.println(getBattleFieldHeight());
        System.out.println(getBattleFieldWidth());
    }

    private void SetNewPoint() {
        // einen neuen Punkt setzen.

        // Die maxmimale höhe ergibt sich durch das spielfeld, damit ich nicht bis an den rand fahre ( gegen die Mauer)
        // zieh ich noch 10 Punkte ab, funktioniert nur leider nicht zuverlässig...
        double maxHeight =  getBattleFieldHeight() - 100;
        double maxWidth = getBattleFieldWidth() - 100 ;

        // Danach das JAVA FUCKING SCHEISS RANDOM KACK, auch hier die 10 Punkte Minimum damit ich nicht gegne die wand fahr
        // geht auch hier noch nicht ( schau aber noch ... )
        double y = Math.random() * maxHeight   + 100;
        double x = Math.random() * maxWidth   + 100;

        // Alten Punkt löschen und neu belegen.
        this.NextPoint = null;
        this.NextPoint = new Point2D.Double(x, y);

        // debug <-<
        System.out.println(NextPoint.getX() + "" + NextPoint.getY());

    }

    @Override
    public void onStatus(StatusEvent e) {
        // null

        // Sollte noch kein Punkt vorhanden sein, kann passieren wenn Status event zum ersten mal aufgerufen wird,
        // dann aufjedenfall einen neuen Punkt setzen,
        if (this.NextPoint == null)
            this.SetNewPoint();

        // sonst einfach fahren und punkte kontrollieren
        goTo();
        handlePoint();


    }

    private void handlePoint() {
        // Methode die alles mir den Punkten / für die Punkte macht.

        // Prüfe ob die Koordinanten erreicht worden sind,


        // X Koordinaten von mir / meinen Ziel holen
        Double myX = getX();
        Double myZielX = this.NextPoint.getX();

        // Y Koordinaten von mir / meinen Ziel holen
        Double myY = getY();
        Double myZielY = this.NextPoint.getY() ;


        // Tipp: es ist kein "double" sondern ein "Double", ich benutze die Klasse damit ich es casten kann,
        // es gab schon Fälle wo meine Koordinate 345,9453835498 war die angeforderte aber 345,9453835361,
        // da diese Stelle unerreichbar ( so kleine schritte konnte ich nicht machen) nun auf ganze integer

        if (getDistanceRemaining()<=0) {
            // Punkt erreicht, neuen Punkt erstellen
            System.out.println("Reach point");
            SetNewPoint();
        } else {
            // Sonst für Debug in die Konsole Printen wo ich gerade bin und wo ich hin will
            System.out.println("my koor: Y " + myY.intValue() + " TO Reach : " + myZielY.intValue());
            System.out.println("my koor: X " + myX.intValue()  + " TO Reach : " + myZielX.intValue());

        }

    }

    private void goTo() {
        // Methode damit ich mich zu meinen Nächsten Punkt fahre....

        // Distance & Winkel holen
        double distance = this.location().distance(NextPoint);
        double angle = normalRelativeAngle(absoluteBearing(location(), NextPoint) - getHeading());
        // Distance und Winkel anpassen
        double[] aangle = getRigthWinkel(angle, distance);

        // Drehen und fahren !
        setTurnRight(aangle[0]);
        setAhead(aangle[1]);
    }

    private Point2D location() {
        // Punkt erstellen mit meinen koordinaten & zurück geben
        return new Point2D.Double(getX(), getY());
    }

    private double absoluteBearing(Point2D source, Point2D target) {
        // WInkelerrechnen ( in Bogenmaß) , mithilfe der Tan funktionen, aus minen Punkt und miene Wunsch punk
        return Math.toDegrees(Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()));
    }

    private double normalRelativeAngle(double angle) {
        // winkel ...
        // mit den Parameter,
        // modullieren...
        //  und irgendwas irgendwie mit den Winkeln machen ... ? O.o

        double relativeAngle = angle % 360;
        if (relativeAngle <= -180)
            return 180 + (relativeAngle % 180);
        else if (relativeAngle > 180)
            return -180 + (relativeAngle % 180);
        else
            return relativeAngle;
    }

    @Override
    public void onHitWall(HitWallEvent event) {

        // wenn ich gegen Die Wand fahre, suche ich mir einen neuen punkt
        SetNewPoint();
        goTo();
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        // auch gegen Robo fahren, such ich mir ein neuen Punkt
        SetNewPoint();
        goTo();
    }
}
