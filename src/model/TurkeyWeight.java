
package model;

/**
 * Az osztály egy objektuma egy heti mérési átlag tárolására képes. Tárolja a
 * mérés hetét, a pulyka nemét, valamint magát a mérési átlagot.
 */
public class TurkeyWeight {
    
    private int week;
    private String turkey;
    private double weighingAverage;
    
    public TurkeyWeight(int week, String turkey, double weighingAverage) {
        this.week=week;
        this.turkey=turkey;
        this.weighingAverage=weighingAverage;
    }
    
    public int getWeek() {
        return week;
    }
    
    public String getTurkey() {
        return turkey;
    }
    
    public double getWeighingAverage() {
        return weighingAverage;
    }
    
    public void setTurkey(String turkey) {
        this.turkey = turkey;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setWeighingAverage(double weighingAverage) {
        this.weighingAverage = weighingAverage;
    }
    
}
