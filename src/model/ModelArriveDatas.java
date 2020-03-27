
package model;

/**
 * Az összes érkezési adat modellje.
 */
public class ModelArriveDatas {
    
    private static ArriveDate arriveDate;
    private static ArriveNumber arriveNumber;
    private static ArriveAge arriveAge;
    private static StockStart stockStart;
    private static UnitFodder unitFodder;
    private static ArriveWeight arriveWeight;
    private static ConsumptionPrescribed cons;

    public static ArriveNumber getArriveNumber() {
        return arriveNumber;
    }

    public static void setArriveNumber(ArriveNumber arriveNumber) {
        ModelArriveDatas.arriveNumber = arriveNumber;
    }

    public static ArriveDate getArriveDate() {
        return arriveDate;
    }

    public static void setArriveDate(ArriveDate arriveDate) {
        ModelArriveDatas.arriveDate = arriveDate;
    }

    public static ArriveAge getArriveAge() {
        return arriveAge;
    }

    public static void setArriveAge(ArriveAge arriveAge) {
        ModelArriveDatas.arriveAge = arriveAge;
    }

    public static StockStart getStockStart() {
        return stockStart;
    }

    public static void setStockStart(StockStart stockStart) {
        ModelArriveDatas.stockStart = stockStart;
    }
    
    public static UnitFodder getUnitFodder() {
        return unitFodder;
    }

    public static void setUnitFodder(UnitFodder unitFodder) {
        ModelArriveDatas.unitFodder = unitFodder;
    }

    public static ConsumptionPrescribed getCons() {
        return cons;
    }

    public static void setCons(ConsumptionPrescribed cons) {
        ModelArriveDatas.cons = cons;
    }

    public static ArriveWeight getArriveWeight() {
        return arriveWeight;
    }

    public static void setArriveWeight(ArriveWeight aw) {
        ModelArriveDatas.arriveWeight = aw;
    }   

}
