
package model;

import java.util.ArrayList;

/**
 * A kezdeti raktárkészlet osztálya.
 */
public class StockStart {
    
    private ArrayList<Integer> stockStart;
    
    public StockStart() {
        this.stockStart = database.Database.selectStockStartDatas();
    }

    public ArrayList<Integer> getStockStart() {
        return stockStart;
    }

    public void setStockStart() {
        this.stockStart = database.Database.selectStockStartDatas();
    }
    
}
