package robowarrior.core;

import robocode.RobocodeFileOutputStream;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author robowiki.net
 * Hilfsklasse um Daten in Datei zu speichern und wieder auszulesen
 * Benutz das ZIP Kompressions Verfahren um Speicherplatz zu sparen
 */
public class FileHandler {

    public static Object readCompressedObject(File filename)
    {
        try
        {
            ZipInputStream zipin = new ZipInputStream(new
                    FileInputStream(filename));
            zipin.getNextEntry();
            ObjectInputStream in = new ObjectInputStream(zipin);
            Object obj = in.readObject();
            in.close();
            return obj;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
        }
        catch (IOException e)
        {
            System.out.println("I/O Exception");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Class not found! :-(");
            e.printStackTrace();
        }
        return null;
    }

    public static void writeObject(Serializable obj, File filename)
    {

        try
        {

            ZipOutputStream zipout = new ZipOutputStream(
                    new RobocodeFileOutputStream(filename));
            zipout.putNextEntry(new ZipEntry(filename.getName()));
            ObjectOutputStream out = new ObjectOutputStream(zipout);


            out.writeObject(obj);
            out.flush();
            zipout.closeEntry();
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Error writing Object:" + e);
        }
    }
}
