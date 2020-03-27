
package model;

/**
 * A heti mérési átlag modelje.
 */
public class ModelTurkeyWeight {
    
    private static TurkeyWeight tw=null;
    
    public static TurkeyWeight getTw() {
        return tw;
    }

    public static void setTw(TurkeyWeight tw) {
        ModelTurkeyWeight.tw = tw;
    }
}