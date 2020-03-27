
package model;

import database.Database;
import interfaces.ModelInterface;
import java.awt.Dialog;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * A fajlagos mutatószámok számításának osztálya.
 */
public class ModelIndexNumber implements ModelInterface {
    
    /**
     * A heti fajlagos mutatószámokat tartalmaző táblázat feltöltése adatokkal.
     */
    public static void tableWeekLoad(DefaultTableModel dtm, Dialog owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyArrive();
        ResultSet rsWeighing = Database.selectWeighing();
        Calendar calToday = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date arriveDate = null;
        int week=1;
        double buckWeightGain=0;
        double henWeightGain=0;
        double allWeightGain=0;
        int buckNum=0;
        int henNum=0;
        int buckCons = 0;
        int henCons = 0;
        double buckIN = 0;
        double henIN = 0;
        double allIN = 0;
        double buckWeight = 0;
        double henWeight = 0;
        try {
            rsWeighing.next();
            rs.next();           
            arriveDate=rs.getDate("date");
            buckWeight=rs.getInt("buck")*rsWeighing.getDouble("buckWeight");
            henWeight=rs.getInt("hen")*rsWeighing.getDouble("henWeight");
            cal.setTime(arriveDate);
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            cal.add(Calendar.DATE, 7);
            while(rsWeighing.next()) {
                //a pulykák darabszámai, illetve a heti valós fogyasztás lekérdezése
                buckNum=Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[BUCK], BUCK);
                henNum=Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[HEN], HEN);
                buckCons=Database.selectConsumptionReal(TURKEY[BUCK], week);
                henCons=Database.selectConsumptionReal(TURKEY[HEN], week);
                if (week>1) {
                    //ráhízás számítása: új adat és előző heti adat különbsége
                    buckWeightGain=rsWeighing.getDouble("buckWeight")-buckWeightGain;
                    henWeightGain=rsWeighing.getDouble("henWeight")-henWeightGain;
                    allWeightGain=rsWeighing.getDouble("allWeight")-allWeightGain;
                    buckIN=buckCons/(buckNum*buckWeightGain);
                    henIN=henCons/((henNum*henWeightGain));
                    allIN=(buckCons+henCons)/(allWeightGain*(buckNum+henNum));
                } else { //ha az első hétről van szó
                    buckIN=buckCons/((buckNum*rsWeighing.getDouble("buckWeight"))-buckWeight);
                    henIN=henCons/((henNum*rsWeighing.getDouble("henWeight"))-henWeight);
                    allIN=(buckCons+henCons)/(rsWeighing.getDouble("allWeight") *(buckNum+henNum)-(buckWeight+henWeight));
                }
                Object[] tableRow = {week, DoubleFormat.doubleFormat(buckIN), DoubleFormat.doubleFormat(henIN),
                    DoubleFormat.doubleFormat(allIN)};
                dtm.addRow(tableRow);
                buckWeightGain=rsWeighing.getDouble("buckWeight");
                henWeightGain=rsWeighing.getDouble("henWeight");
                allWeightGain=rsWeighing.getDouble("allWeight");
                cal.add(Calendar.DATE, 7);
                week++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A heti fajlagos mutatószámokkal kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
    /**
     * Az összesített fajlagos mutatószámokat tartalmaző táblázat feltöltése
     * adatokkal.
     */
    public static void tableCumLoad(DefaultTableModel dtm, Dialog owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyArrive();
        ResultSet rsWeighing = Database.selectWeighing();
        Calendar calToday = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date arriveDate = null;
        int week=1;
        double buckWeightGain=0;
        double henWeightGain=0;
        double allWeightGain=0;
        int buckNum=0;
        int henNum=0;
        int buckCons = 0;
        int henCons = 0;
        double buckIN = 0;
        double henIN = 0;
        double allIN = 0;
        double buckWeight = 0;
        double henWeight = 0;
        try {
            rsWeighing.next();
            rs.next();           
            arriveDate=rs.getDate("date");
            buckWeight=rs.getInt("buck")*rsWeighing.getDouble("buckWeight");
            henWeight=rs.getInt("hen")*rsWeighing.getDouble("henWeight");
            cal.setTime(arriveDate);
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            cal.add(Calendar.DATE, 7);
            while(rsWeighing.next()) {
                buckNum=Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[BUCK], BUCK);
                henNum=Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[HEN], HEN);
                buckCons+=Database.selectConsumptionReal(TURKEY[BUCK], week);
                henCons+=Database.selectConsumptionReal(TURKEY[HEN], week);
                if (week>1) {
                    buckWeightGain+=(rsWeighing.getDouble("buckWeight")-buckWeightGain);
                    henWeightGain+=(rsWeighing.getDouble("henWeight")-henWeightGain);
                    allWeightGain+=(rsWeighing.getDouble("allWeight")-allWeightGain);
                    buckIN=buckCons/((buckNum*buckWeightGain)-buckWeight);
                    henIN=henCons/((henNum*henWeightGain)-henWeight);
                    allIN=(buckCons+henCons)/((allWeightGain*(buckNum+henNum))-(buckWeight+henWeight));
                } else {
                    buckIN=buckCons/((buckNum*rsWeighing.getDouble("buckWeight"))-buckWeight);
                    henIN=henCons/((henNum*rsWeighing.getDouble("henWeight"))-henWeight);
                    allIN=(buckCons+henCons)/(rsWeighing.getDouble("allWeight") *(buckNum+henNum)-(buckWeight+henWeight));
                }
                Object[] tableRow = {week, DoubleFormat.doubleFormat(buckIN), DoubleFormat.doubleFormat(henIN),
                    DoubleFormat.doubleFormat(allIN)};
                dtm.addRow(tableRow);
                buckWeightGain=rsWeighing.getDouble("buckWeight");
                henWeightGain=rsWeighing.getDouble("henWeight");
                allWeightGain=rsWeighing.getDouble("allWeight");
                cal.add(Calendar.DATE, 7);
                week++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Az összesített fajlagos mutatószámokkal kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
}
