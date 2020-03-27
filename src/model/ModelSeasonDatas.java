
package model;

import java.util.ArrayList;

/**
 * A szezon kezdetekor létrejön egy ModelSeasonDatas objektum, amit átadok a
 * ModelSeason osztálynak, hogy létrehozza a szezont. A ModelSeasonDatas
 * osztály egy példánya tartalmazza a szezon kezdéséhez szükséges adatokat.
 */
public class ModelSeasonDatas {
    
    private String arriveDate;
    private int age;
    private int buckNumber;
    private int henNumber;
    private int buckStock;
    private int henStock;
    private int onStock;
    private double unit;
    private double buckWeight;
    private double henWeight;
    private ArrayList<Double> buckConsList;
    private ArrayList<Double> henConsList;
    
    public ModelSeasonDatas(String date, int age, int buckNum, int henNum, int buckStock, int henStock,
            int onStock, double unit, double buckWeight, double henWeight,
            ArrayList<Double> buckConsList, ArrayList<Double> henConsList) {
        this.arriveDate=date;
        this.age=age;
        this.buckNumber=buckNum;
        this.henNumber=henNum;
        this.buckStock=buckStock;
        this.henStock=henStock;
        this.onStock=onStock;
        this.unit=unit;
        this.buckWeight=buckWeight;
        this.henWeight=henWeight;
        this.buckConsList=buckConsList;
        this.henConsList=henConsList;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public ArrayList<Double> getBuckConsList() {
        return buckConsList;
    }

    public void setBuckConsList(ArrayList<Double> buckConsList) {
        this.buckConsList = buckConsList;
    }

    public int getBuckNumber() {
        return buckNumber;
    }

    public void setBuckNumber(int buckNumber) {
        this.buckNumber = buckNumber;
    }

    public int getBuckStock() {
        return buckStock;
    }

    public void setBuckStock(int buckStock) {
        this.buckStock = buckStock;
    }

    public double getBuckWeight() {
        return buckWeight;
    }

    public void setBuckWeight(double buckWeight) {
        this.buckWeight = buckWeight;
    }

    public ArrayList<Double> getHenConsList() {
        return henConsList;
    }

    public void setHenConsList(ArrayList<Double> henConsList) {
        this.henConsList = henConsList;
    }

    public int getHenNumber() {
        return henNumber;
    }

    public void setHenNumber(int henNumber) {
        this.henNumber = henNumber;
    }

    public int getHenStock() {
        return henStock;
    }

    public void setHenStock(int henStock) {
        this.henStock = henStock;
    }

    public double getHenWeight() {
        return henWeight;
    }

    public void setHenWeight(double henWeight) {
        this.henWeight = henWeight;
    }

    public int getOnStock() {
        return onStock;
    }

    public void setOnStock(int onStock) {
        this.onStock = onStock;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

}
