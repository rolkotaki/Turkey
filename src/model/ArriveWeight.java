
package model;

import java.util.ArrayList;

/**
 * A bakok és tojók kezdeti tömegét tárolja az osztály.
 */
public class ArriveWeight {
    
    private ArrayList<Double> weightList;
    
    public ArriveWeight() {
        this.weightList = database.Database.selectArriveWeights();
    }

    public ArrayList<Double> getWeightList() {
        return weightList;
    }

    public void setWeightList() {
        this.weightList = database.Database.selectArriveWeights();
    }
    
}
