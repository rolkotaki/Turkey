package interfaces;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Interfész a modell réteg számára.
 */
public interface ModelInterface {
    public static final File SEASONINI = new File("./files/season.ini");
    public static final String SEASONON = "on";
    public static final String SEASONOFF = "off";    
    public static final String[] DAYS = {"vasárnap","hétfő","kedd","szerda","csütörtök","péntek","szombat"};
    public static final String[] TURKEY = {"buck","hen","allturkey"};
    public static final int BUCK = 0;
    public static final int HEN = 1;
    public static final int ALL = 2;
    public static final SimpleDateFormat SDFDATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DATEFORMAT = "yyyy-MM-dd";
    public static final int AGE = 0;
    public static final int BUCK_NUMBER = 1;
    public static final int HEN_NUMBER = 2;
    public static final int BUCK_WEIGHT = 0;
    public static final int HEN_WEIGHT = 1;
    public static final String[] SQLFUNCTION = {"SUM","AVG","MIN","MAX"};
    public static final int SUM = 0;
    public static final int AVG = 1;
    public static final int MIN = 2;
    public static final int MAX = 3;
}
