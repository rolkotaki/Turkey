
package model;

/**
 * Az osztály egyetlen osztálymetódusa egy paraméterként kapott double számot
 * úgy formáz meg, hogy annak maximum három tizedesjegye legyen.
 */
public class DoubleFormat {
    
    public static double doubleFormat(double d) {
        int intNum=(int)(d*1000);
        return (intNum/1000.0);
    }
    
}
