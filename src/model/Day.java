
package model;

import interfaces.ModelInterface;
import java.util.Calendar;
import java.util.Date;

/**
 * Az osztály osztálymetódusa szöveges formában visszaadja, hogy a hét melyik
 * napja van egy adott dátumban.
 */
public class Day implements ModelInterface {
    
    public static String dayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int d=cal.get(Calendar.DAY_OF_WEEK);
        String day;
        switch (d) {
            case 1: day = DAYS[0]; break;
            case 2: day = DAYS[1]; break;
            case 3: day = DAYS[2]; break;
            case 4: day = DAYS[3]; break;
            case 5: day = DAYS[4]; break;
            case 6: day = DAYS[5]; break;
            case 7: day = DAYS[6]; break;
            default: day = DAYS[0]; break;
        }
        return day;
    }
    
}
