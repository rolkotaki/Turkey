
package model;

import interfaces.ModelInterface;
import java.util.Date;

/**
 * A pulykák érkézének dátumát tárolja az alábbi osztály adattagja.
 */
public class ArriveDate {
    
    private Date arriveDate;
    
    public ArriveDate() {
        this.arriveDate=database.Database.selectArriveDate();
    }

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate() {
        this.arriveDate = database.Database.selectArriveDate();
    }
    
    @Override
    public String toString() {
        return ModelInterface.SDFDATE.format(arriveDate);        
    }
}
