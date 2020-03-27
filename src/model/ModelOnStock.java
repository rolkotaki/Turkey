
package model;

import database.Database;

/**
 * A model tárolja, hogy aktuálisan hány kilogramm takarmány van raktáron.
 */
public class ModelOnStock {
    
    private static int onStock;

    public static int getOnStock() {
        return onStock;
    }

    public static void setOnStock() {
        ModelOnStock.onStock = Database.selectStockNow();
    }
    
}
