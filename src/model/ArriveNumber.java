
package model;

import java.util.ArrayList;

/**
 * Az osztály lista adattagja tárolja a pulykák érkezési létszámát.
 */
public class ArriveNumber {
   
    private ArrayList<Integer> arriveNumber;
    
    public ArriveNumber() {
        this.arriveNumber=database.Database.selectArriveNumber();
    }

    public ArrayList<Integer> getArriveNumber() {
        return arriveNumber;
    }

    public void setArriveNumber() {
        this.arriveNumber = database.Database.selectArriveNumber();
    }
    
}
