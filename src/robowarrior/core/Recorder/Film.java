package robowarrior.core.Recorder;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jens on 05.03.2015.
 */
// Hält ein Array von Picture Elementen und führt Operationen darauf aus
public class Film {

   public ArrayList<Picture> pictures=new ArrayList<Picture>();


   public void Film(){

    }
    public void draw(Graphics2D g){
        int count = pictures.size();
        int[] x=new int[count];
        int[] y=new int[count];
        int z=0;
        for(Picture p: pictures){
            x[z]=(int)p.getX();
            y[z]=(int)p.getY();
            g.drawOval(x[z],y[z],5,5);
            z++;
        }

        g.drawPolyline(x,y,count);
    }
    public void addPicture(Picture pic){
        Picture last=getLastPicture();
        //Nur hinzufügen, wenn der Gegner sich bewegt hat
        if(!last.equals(pic)){
            this.pictures.add(pic);
        }

    }
    // Letzten Frame zurückgeben
    public Picture getLastPicture(){
        if(pictures.size()<=0){
            return new Picture(0);
        } else {
            return pictures.get((pictures.size()-1));
        }

    }
    public ArrayList<String> serialize(){
        ArrayList<String> list=new ArrayList<String>();
        for(Picture i :pictures){
            list.add(""+i.getX()+","+i.getY()+","+i.getHeading()+","+i.getVelocity()+","+i.getTime());
        }
        return list;

    }
}
