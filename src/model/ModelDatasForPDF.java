package model;

/**
 * A szezon végén készített PDF kimutatás számára szükséges adatok modelje.
 */
public class ModelDatasForPDF {

    private String file;
    private int buckArriveNum;
    private int henArriveNum;
    private int buckSellNum;
    private int henSellNum;
    private int buckArriveWeight;
    private int henArriveWeight;
    private int buckSellWeight;
    private int henSellWeight;
    private int loss;
    private double lossPerCent;
    private int allReceivedFodder;
    private int allWeightGain;
    private double cumIndexNumber;
    private String seasonName;

    public ModelDatasForPDF() {
        this.file = null;
        this.buckArriveNum = 0;
        this.henArriveNum = 0;
        this.buckSellNum = 0;
        this.henSellNum = 0;
        this.buckArriveWeight = 0;
        this.henArriveWeight = 0;
        this.buckSellWeight = 0;
        this.henSellWeight = 0;
        this.loss = 0;
        this.lossPerCent = 0;
        this.allReceivedFodder = 0;
        this.allWeightGain = 0;
        this.cumIndexNumber = 0;
        this.seasonName="seasonName";
    }

    public int getAllReceivedFodder() {
        return allReceivedFodder;
    }

    public void setAllReceivedFodder(int allReceivedFodder) {
        this.allReceivedFodder = allReceivedFodder;
    }

    public int getAllWeightGain() {
        return allWeightGain;
    }

    public void setAllWeightGain(int allWeightGain) {
        this.allWeightGain = allWeightGain;
    }

    public int getBuckArriveNum() {
        return buckArriveNum;
    }

    public void setBuckArriveNum(int buckArriveNum) {
        this.buckArriveNum = buckArriveNum;
    }

    public int getBuckArriveWeight() {
        return buckArriveWeight;
    }

    public void setBuckArriveWeight(int buckArriveWeight) {
        this.buckArriveWeight = buckArriveWeight;
    }

    public int getBuckSellNum() {
        return buckSellNum;
    }

    public void setBuckSellNum(int buckSellNum) {
        this.buckSellNum = buckSellNum;
    }

    public int getBuckSellWeight() {
        return buckSellWeight;
    }

    public void setBuckSellWeight(int buckSellWeight) {
        this.buckSellWeight = buckSellWeight;
    }

    public double getCumIndexNumber() {
        return cumIndexNumber;
    }

    public void setCumIndexNumber(double cumIndexNumber) {
        this.cumIndexNumber = cumIndexNumber;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getHenArriveNum() {
        return henArriveNum;
    }

    public void setHenArriveNum(int henArriveNum) {
        this.henArriveNum = henArriveNum;
    }

    public int getHenArriveWeight() {
        return henArriveWeight;
    }

    public void setHenArriveWeight(int henArriveWeight) {
        this.henArriveWeight = henArriveWeight;
    }

    public int getHenSellNum() {
        return henSellNum;
    }

    public void setHenSellNum(int henSellNum) {
        this.henSellNum = henSellNum;
    }

    public int getHenSellWeight() {
        return henSellWeight;
    }

    public void setHenSellWeight(int henSellWeight) {
        this.henSellWeight = henSellWeight;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public double getLossPerCent() {
        return lossPerCent;
    }

    public void setLossPerCent(double lossPerCent) {
        this.lossPerCent = lossPerCent;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

}
