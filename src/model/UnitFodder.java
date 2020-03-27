
package model;

/**
 * A mértékegység osztály. Egyetlen adattagja a zsák-kilogramm közötti
 * váltószám.
 */
public class UnitFodder {
    
    private double unit;
    
    public UnitFodder() {
        this.unit=database.Database.selectUnitFodder();
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit() {
        this.unit = database.Database.selectUnitFodder();
    }
    
}
