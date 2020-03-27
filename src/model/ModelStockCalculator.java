package model;

import database.Database;
import interfaces.ModelInterface;
import java.util.Calendar;
import java.util.Date;

/**
 * Az osztály objektuma kiszámolja, hogy meddig elég még a ratkáron lévő
 * takarmánymennyiség. Ezt a napi előírt fogyasztási mennyiségek segítségével
 * határozza meg.
 */
public class ModelStockCalculator {

    private int onStock;
    private int enoughDays;
    private Date orderDate;
    private String message;

    public ModelStockCalculator() {
        message = null;
        setOnStock(Database.selectStockNow());
        setEnoughDays(countEnoughDays());
        setOrderDate(orderDate());
        if (message == null) {
            String m = "<html><b>A raktáron lévő takarmány a mai nappal együtt még<br>"
                    + getEnoughDays() + " napig elég. Legkésőbb "
                    + ModelInterface.SDFDATE.format(getOrderDate()) + "-n rendeljen takarmányt!</b></html>";
            setMessage(m);
        }
    }

    /**
     * Annak a meghatározása, hogy a mai nappal együtt még hány napig elég a
     * raktáron lévő takarmány.
     */
    private int countEnoughDays() {
        Date arriveDate = Database.selectArriveDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(arriveDate);
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        int buck = Database.selectTurkeyNumber(ModelInterface.TURKEY[ModelInterface.BUCK]);
        int hen = Database.selectTurkeyNumber(ModelInterface.TURKEY[ModelInterface.HEN]);
        int week = 1;
        int day = Database.selectArriveAge();
        int restDay = 0;
        int prescribed = 0;
        int stock = getOnStock();
        int enough = 0;
        while (cal.getTime().before(calToday.getTime())) {
            cal.add(Calendar.DATE, 1);
            day++;
        }
        //az aktuális hét, a hétből már eltelt nap, valamint az egy napra előírt fogyasztási mennyiség
        //meghatározása
        week = ((day - 1) / 7) + 1;
        restDay = day % 7;
        prescribed = (int) ((buck * (Database.selectConsPrescribed(ModelInterface.TURKEY[ModelInterface.BUCK], week) / 7.0))
                + (hen * (Database.selectConsPrescribed(ModelInterface.TURKEY[ModelInterface.HEN], week) / 7.0)));
        while (stock > 0) { //amíg a takarmánymennyiség nagyobb nullánál
            try {
                if (restDay == 7) { //ha eltelt egy újabb hét
                    week++;
                    restDay = 0;
                    prescribed = (int) ((buck * (Database.selectConsPrescribed(ModelInterface.TURKEY[ModelInterface.BUCK], week) / 7.0))
                            + (hen * (Database.selectConsPrescribed(ModelInterface.TURKEY[ModelInterface.HEN], week) / 7.0)));
                } else {
                    restDay++;
                }
                //a takarmánymennyiségből kivonom a napi előírt mennyiséget, majd növelem a napok számát
                stock -= prescribed;
                enough++;
            } catch (Exception e) {
                String m = "<html><font color=\"green\"><b>A raktáron lévő takarmány végig elég lesz!</b></font></html>";
                setMessage(m);
                break;
            }
        }
        --enough;
        return enough;
    }

    /**
     * A rendelési dátum meghatározása. Bele kell kalkulálni, hogy a rendelés
     * körülbelül egy hét.
     */
    private Date orderDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, getEnoughDays() - 8);
        return cal.getTime();
    }

    private int getOnStock() {
        return onStock;
    }

    public void setOnStock(int onStock) {
        this.onStock = onStock;
    }

    public int getEnoughDays() {
        return enoughDays;
    }

    public void setEnoughDays(int enoughDays) {
        this.enoughDays = enoughDays;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
