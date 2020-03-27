
package model;

import java.util.ArrayList;

/**
 * A pulykák számára előírt fogyasztási mennyiségek osztálya. A két adattag
 * tárolja a bakok és tojók számára előírt fogyasztási mennyiségeket.
 */
public class ConsumptionPrescribed {
    
    private ArrayList<Double> buckCons;
    private ArrayList<Double> henCons;
    
    public ConsumptionPrescribed() {
        this.buckCons=database.Database.selectBuckPrescribedCons();
        this.henCons=database.Database.selectHenPrescribedCons();
    }

    public ArrayList<Double> getBuckCons() {
        return buckCons;
    }

    public void setBuckCons() {
        this.buckCons = database.Database.selectBuckPrescribedCons();
    }

    public ArrayList<Double> getHenCons() {
        return henCons;
    }

    public void setHenCons() {
        this.henCons = database.Database.selectHenPrescribedCons();
    }
    
}
