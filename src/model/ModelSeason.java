
package model;

import database.Database;
import interfaces.ModelInterface;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Az osztály egy logikai változóban tárolja, hogy jelenleg van-e
 * aktuálisan futó szezon, vagy nincs. Továbbá az alábbi osztály végzi az ini
 * fájl módosítását is.
 */
public class ModelSeason {
    
    public static boolean seasonOn=false;
    private static Properties season = new Properties();
    
    public static boolean isSeasonOn() {
        FileInputStream fis=null;
        try {
            fis = new FileInputStream(ModelInterface.SEASONINI);
            season.load(fis);
            fis.close();
        } catch (IOException io) {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }
            }
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (Exception e) {

                }
            }
        }
        if (season.getProperty("season").equals(ModelInterface.SEASONON)) {
            seasonOn=true;
        } else {
            seasonOn = false;
        }
        return seasonOn;
    }

    private static void setSeasonOn(boolean b) {
        String s=null;
        if (b)
            s=ModelInterface.SEASONON;
        else
            s=ModelInterface.SEASONOFF;
        FileOutputStream fos=null;
        try {
           season.setProperty("season", s);
           fos = new FileOutputStream(ModelInterface.SEASONINI);
           season.store(fos, "");
           fos.close();
        } catch (IOException io) {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (Exception e) {

                }
            }
        } finally {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (Exception e) {

                }
            }
        }
        seasonOn = b;
    }
    
    public static boolean createSeason(ModelSeasonDatas msd) {
        boolean ok=Database.insertInitialDatas(msd.getArriveDate(), msd.getAge(), msd.getBuckNumber(),
                msd.getHenNumber(), msd.getBuckStock(), msd.getHenStock(), msd.getUnit(),
                msd.getBuckConsList(), msd.getHenConsList(), msd.getBuckWeight(),
                msd.getHenWeight(), msd.getOnStock());
        if(ok)
            setSeasonOn(true);
        return ok;
    }
    
    public static boolean finishSeason(String path) {
        ModelPDF.exportToPDF(path);
        ModelExcel.exportAll(path);
        boolean ok=Database.deleteAll();
        if(ok)
            setSeasonOn(false);
        return ok;
    }
    
}

/*
 * Forrás: Kaczur Sándor Programozási technológia című könyvének 19. fejezete
 * (AblakMéretPozíció2.java mintapélda)
 */
