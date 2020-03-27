package model;

import database.Database;
import interfaces.ModelInterface;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * A ModelTable osztály metódusai a különböző táblázatok betöltését és mentését
 * hajtják végre. 
 */
public class ModelTable implements ModelInterface {

    /**
     * A pulykák létszámával kapcsolatos adattábla betöltése.
     */
    public static void tableBasicLoad(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyArrive();
        ResultSet rs2 = Database.selectTurkeyLost();
        Database.updateTurkeyNumber();
        Date maxDate = null;
        Calendar calToday = Calendar.getInstance();
        Date arriveDate = null;
        int age = 0;
        int buck = 0;
        int hen = 0;
        try {
            rs.next();
            arriveDate = rs.getDate("date");
            age = rs.getInt("age");
            while (rs2.next()) {
                Object[] tableRow = {rs2.getString("date"), Day.dayOfWeek(rs2.getDate("date")), age, (((age - 1) / 7) + 1),
                    Database.selectTurkeyNumberByDate(rs2.getDate("date"), TURKEY[BUCK], BUCK),
                    Database.selectTurkeyNumberByDate(rs2.getDate("date"), TURKEY[HEN], HEN),
                    rs2.getInt("deathBuck"), rs2.getInt("deathHen"), rs2.getInt("faultyBuck"), rs2.getInt("faultyHen"),
                    ((rs2.getInt("faultyBuck")) + (rs2.getInt("faultyHen")) + (rs2.getInt("deathBuck")) + (rs2.getInt("deathHen")))};
                dtm.addRow(tableRow);
                age++;
            }
            buck = Database.selectTurkeyNumber(TURKEY[BUCK]);
            hen = Database.selectTurkeyNumber(TURKEY[HEN]);
            Calendar cal = Calendar.getInstance();
            if (Database.maxDateTurkeyLost() != null) {
                maxDate = Database.maxDateTurkeyLost();
                cal.setTime(maxDate);
                cal.add(Calendar.DATE, 1);
            } else {
                maxDate = arriveDate;
                cal.setTime(maxDate);
            }
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            while (cal.getTime().before(calToday.getTime())) {
                Object[] tableRow = {SDFDATE.format(cal.getTime()), Day.dayOfWeek(cal.getTime()), age, ((int) (age / 7)), buck, hen,
                    0, 0, 0, 0, 0};
                dtm.addRow(tableRow);
                age++;
                cal.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A létszámmal kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        ModelDatasForExcel.setDtmBasic(dtm);
    }

    /**
     * A pulykák létszámával kapcsolatos adattáblában található adatokat elmenti
     * az adatbázisba, természetesen az adatbázis osztály segítségével.
     */
    public static void tableBasicSave(DefaultTableModel dtm, Frame owner) {
        boolean ok = true;
        try {
            int lossBuck=0;
            int lossHen=0;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                lossBuck+=Integer.parseInt(dtm.getValueAt(i, 6).toString());
                lossHen+=Integer.parseInt(dtm.getValueAt(i, 7).toString());
                lossBuck+=Integer.parseInt(dtm.getValueAt(i, 8).toString());
                lossHen+=Integer.parseInt(dtm.getValueAt(i, 9).toString());
            }
            if (lossBuck>Database.selectTurkeyNumber(TURKEY[BUCK]) ||
                    lossHen>Database.selectTurkeyNumber(TURKEY[HEN])) {
                JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Az elhullás nem "
                        + "lehet több, mint az összes pulyka darabszáma!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                ok = false;
            }
                
        } catch (Exception x) {
            ok = false;
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Minden cellát ki kell tölteni!"
                    + "<br>Az érték legalább 0!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
        }
        if (ok) {
            Database.deleteTurkeyLost();
            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (Database.insertTurkeyLost(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 8).toString(),
                        dtm.getValueAt(i, 9).toString(), dtm.getValueAt(i, 6).toString(),
                        dtm.getValueAt(i, 7).toString()))
                    ;
            }
            Database.updateTurkeyNumber();
            dtm.setRowCount(0);
            tableBasicLoad(dtm, owner);
        }
    }

    /**
     * A pulykák fogyasztásával kapcsolatos adatok betöltése a megfelelő
     * táblázatokba.
     */
    public static void tableConsumptionLoad(String turkey, DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rs = Database.selectFodderConsumption(turkey);
        int index = -1;
        switch (turkey) {
            case "buck":
                index = 0;
                break;
            case "hen":
                index = 1;
                break;
        }
        double unitFodder = Database.selectUnitFodder();
        int turkeyNumber = 0;
        Date arriveDate = null;
        Date maxDate = null;
        Calendar calToday = Calendar.getInstance();
        try {
            arriveDate = Database.selectArriveDate();
            int age = 0;
            age = Database.selectArriveAge();
            int rowWeek = 1;
            int row = 0;
            int week = ((age - 1) / 7) + 1;
            double prescribedDay = 0;
            int allFodder = 0;
            double allPrescribed = 0;
            double prescribedWeek = 0;
            int consRealWeek = 0;
            int allWeek = 0;
            int allWeekReal = 0;
            int allDifference = 0;
            while (rs.next()) {
                turkeyNumber = Database.selectTurkeyNumberByDate(rs.getDate("date"), turkey, index);
                rowWeek = (int) ((row / 7) + 1);
                prescribedDay = (((double) (Database.selectConsPrescribed(turkey, week))) / 7);
                allFodder += rs.getInt("fodder");
                allPrescribed += (turkeyNumber * prescribedDay);
                prescribedWeek += (turkeyNumber * prescribedDay);
                if ((row + 1) % 7 == 0) {
                    consRealWeek = Database.selectConsumptionReal(turkey, rowWeek);
                    allWeek += (int) prescribedWeek;
                    allWeekReal += consRealWeek;
                    allDifference += prescribedWeek - consRealWeek;
                    Object[] tableRow = {rs.getString("date"), (prescribedDay * 7), prescribedDay, (rs.getInt("fodder") / unitFodder),
                        rs.getInt("fodder"), allFodder, ((int) (turkeyNumber * prescribedDay)), ((int) allPrescribed), ((int) prescribedWeek),
                        consRealWeek, ((int) prescribedWeek - consRealWeek), allWeek, allWeekReal, allDifference};
                    dtm.addRow(tableRow);
                    prescribedWeek = 0;
                    row++;
                } else {
                    Object[] tableRow = {rs.getString("date"), (prescribedDay * 7), prescribedDay, (rs.getInt("fodder") / unitFodder),
                        rs.getInt("fodder"), allFodder, ((int) (turkeyNumber * prescribedDay)), ((int) allPrescribed), null, null, null,
                        null, null, null};
                    dtm.addRow(tableRow);
                    row++;
                }
                age++;
                week = ((age - 1) / 7) + 1;
            }
            Calendar cal = Calendar.getInstance();
            if (Database.maxDateFodderConsumption(turkey) != null) {
                maxDate = Database.maxDateFodderConsumption(turkey);
                cal.setTime(maxDate);
                cal.add(Calendar.DATE, 1);
            } else {
                maxDate = arriveDate;
                cal.setTime(maxDate);
            }
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            while (cal.getTime().before(calToday.getTime())) {
                turkeyNumber = Database.selectTurkeyNumberByDate(cal.getTime(), turkey, index);
                rowWeek = (int) ((row / 7) + 1);
                prescribedDay = (((double) (Database.selectConsPrescribed(turkey, week))) / 7);
                allPrescribed += (turkeyNumber * prescribedDay);
                prescribedWeek += (turkeyNumber * prescribedDay);
                if ((row + 1) % 7 == 0) {
                    consRealWeek = Database.selectConsumptionReal(turkey, rowWeek);
                    allWeek += (int) prescribedWeek;
                    allWeekReal += consRealWeek;
                    allDifference += prescribedWeek - consRealWeek;
                    Object[] tableRow = {SDFDATE.format(cal.getTime()), (prescribedDay * 7), prescribedDay,
                        0, 0, allFodder, ((int) (turkeyNumber * prescribedDay)), ((int) allPrescribed), ((int) prescribedWeek),
                        consRealWeek, ((int) (prescribedWeek - consRealWeek)), ((int) allPrescribed), allWeek, allWeekReal, allDifference};
                    dtm.addRow(tableRow);
                    prescribedWeek = 0;
                    row++;
                } else {
                    Object[] tableRow = {SDFDATE.format(cal.getTime()), (prescribedDay * 7), prescribedDay, 0,
                        0, allFodder, ((int) (turkeyNumber * prescribedDay)), ((int) allPrescribed), null, null, null, null, null, null};
                    dtm.addRow(tableRow);
                    row++;
                }
                cal.add(Calendar.DATE, 1);
                age++;
                week = ((age - 1) / 7) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A fogyasztással kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        switch (turkey) {
            case "buck":
                ModelDatasForExcel.setDtmBuckCons(dtm);
                break;
            case "hen":
                ModelDatasForExcel.setDtmHenCons(dtm);
                break;
        }
    }

    /**
     * A pulykák fogyasztásával kapcsolatos adatok elmentése az adatbázisba, a
     * megfelelő táblázatokból. Itt fontos figyelni, hogy ne lehessen több a
     * kivitt takarmány, mint a raktáron adott időpontban lévő mennyiség,
     * valamint a heti fogyasztások mennyisége ne lehessen több, mint az addig
     * kivitt takarmánymennyiség.
     */
    public static void tableConsumptionSave(String turkey, DefaultTableModel dtm, Frame owner) {
        boolean ok = true;
        int fodder = 0;
        int stockAll = 0;
        int stockStart = 0;
        int buckOrHen = -1;
        int cons = 0;
        stockStart = Database.selectStockStart();
        if (turkey.equals(TURKEY[BUCK])) {
            buckOrHen = 1;
        } else if (turkey.equals(TURKEY[HEN])) {
            buckOrHen = 0;
        }
        try {
            //annak ellenőrzése, hogy az összes kivitt takarmány több-e, mint a lehetséges
            for (int i = 0; i < dtm.getRowCount(); i++) {
                fodder += Integer.parseInt(dtm.getValueAt(i, 4).toString());
            }
            stockAll += stockStart;
            stockAll += Database.selectAllReceivedFodder();
            stockAll -= Database.selectAllFodderCons(TURKEY[buckOrHen]);
            if (stockAll < fodder) {
                ok = false;
            }
            if (ok) { //ha az első feltételt teljesítik az adatok
                fodder = 0;
                stockAll = 0;
                Date date;
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    date = SDFDATE.parse(dtm.getValueAt(i, 0).toString());
                    fodder += Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    stockAll = stockStart;
                    stockAll += Database.selectAllReceivedFodderByDate(date);
                    stockAll -= Database.selectAllFodderConsByDate(TURKEY[buckOrHen], date);
                    //a heti valós fogyasztás ellenőrzése
                    if (((i+1)%7)==0) {
                        cons += Integer.parseInt(dtm.getValueAt(i, 9).toString());
                        if (cons > fodder) {
                            ok = false;
                            break;
                        }
                    }
                    //egy adott ideig kivitt takarmánymennyiség ellenőrzése
                    if (stockAll < fodder) {
                        ok = false;
                        break;
                    }
                }
            }
        } catch (Exception x) {
            ok = false;
            Database.closeConnection();
        }
        if (ok) {
            Database.deleteFodderConsumption(turkey);
            Database.deleteConsumptionReal(turkey);
            int week = 0;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (Database.insertFodderConsumption(turkey, dtm.getValueAt(i, 0).toString(),
                        dtm.getValueAt(i, 4).toString()))
                    ;
                if ((i + 1) % 7 == 0) {
                    week = (int) ((i / 7) + 1);
                    Database.insertConsumptionReal(turkey, Integer.toString(week), dtm.getValueAt(i, 9).toString());
                }
            }
            Database.updateStockNow();
            dtm.setRowCount(0);
            tableConsumptionLoad(turkey, dtm, owner);
        } else {
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A kivitt takarmány mennyisége nem lehet több, mint a raktáron lévő aktuális mennyiség!<br>"
                    + "A heti valós fogyasztás nem lehet több a kivitt takarmánymennyiségnél!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
        }
        Database.closeConnection();
    }

    /**
     * A bakok és tojók együttes adataival kapcsolatos adatok betöltése a
     * megfelelő táblázatba.
     */
    public static void tableBuckHenLoad(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyArrive();
        Date arriveDate = null;
        Date today = new Date();
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        Calendar cal = Calendar.getInstance();
        try {
            int buckNumber = 0;
            int henNumber = 0;
            int turkeyNumber = 0;
            int lost = 0;
            int allLost = 0;
            int fodder = 0;
            int fodderAll = 0;
            double prescribed = 0;
            double prescribedAll = 0;
            double prescribedWeek = 0;
            int consRealWeek = 0;
            int allWeek = 0;
            int allWeekReal = 0;
            int allDifference = 0;
            rs.next();
            arriveDate = rs.getDate("date");
            int age = 0;
            age = rs.getInt("age");
            int week = ((age - 1) / 7) + 1;
            cal.setTime(arriveDate);
            int rowWeek = 1;
            int row = 0;
            while (cal.getTime().before(calToday.getTime())) {
                rowWeek = (int) ((row / 7) + 1);
                buckNumber = Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[BUCK], BUCK);
                henNumber = Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[HEN], HEN);
                turkeyNumber = buckNumber + henNumber;
                lost = Database.selectDayTurkeyLost(TURKEY[BUCK], cal.getTime()) + Database.selectDayTurkeyLost(TURKEY[HEN], cal.getTime());
                allLost += lost;
                fodder = Database.selectFodderConsByDate(TURKEY[BUCK], cal.getTime(), cal.getTime())
                        + Database.selectFodderConsByDate(TURKEY[HEN], cal.getTime(), cal.getTime());
                fodderAll += fodder;
                prescribed = ((((Database.selectConsPrescribed(TURKEY[BUCK], week) * (buckNumber))
                        + (Database.selectConsPrescribed(TURKEY[HEN], week) * (henNumber))) / 7.0));
                prescribedAll += prescribed;
                prescribedWeek += prescribed;
                if ((row + 1) % 7 == 0) {
                    consRealWeek = (Database.selectConsumptionReal(TURKEY[BUCK], rowWeek) + Database.selectConsumptionReal(TURKEY[HEN], rowWeek));
                    allWeek += (int) prescribedWeek;
                    allWeekReal += (int) consRealWeek;
                    allDifference += (int) (prescribedWeek - consRealWeek);
                    Object[] tableRow = {SDFDATE.format(cal.getTime()), turkeyNumber, lost, allLost, fodder, fodderAll, ((int) prescribed), ((int) prescribedAll),
                        ((int) prescribedWeek), consRealWeek, ((int) (prescribedWeek - consRealWeek)),
                        allWeek, allWeekReal, allDifference};
                    dtm.addRow(tableRow);
                    prescribedWeek = 0;
                    row++;
                } else {
                    Object[] tableRow = {SDFDATE.format(cal.getTime()), turkeyNumber, lost, allLost, fodder, fodderAll, ((int) prescribed), ((int) prescribedAll),
                        null, null, null, null, null, null};
                    dtm.addRow(tableRow);
                    row++;
                }
                cal.add(Calendar.DATE, 1);
                age++;
                week = ((age - 1) / 7) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A bakok és tojók együttes adatainak betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        ModelDatasForExcel.setDtmBuckHen(dtm);
    }

    /**
     * A bakok és tojók együttes adatainak frissítése a megfelelő táblázatban.
     */
    public static void tableBuckHenUpdate(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        tableBuckHenLoad(dtm, owner);
    }

    /**
     * A raktárkészlettel kapcsolatos táblázat betöltése.
     */
    public static void tableStockLoad(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rsStock = Database.selectStock();
        Date arriveDate = Database.selectArriveDate();
        Date maxDate = null;
        Calendar calToday = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        int stock = 0;
        try {
            while (rsStock.next()) {
                Object[] tableRow = {rsStock.getString("date"), Database.selectStockByDate(rsStock.getDate("date")),
                    rsStock.getInt("fodderOrder"), rsStock.getInt("fodderReceive"), rsStock.getInt("strawBuck"),
                    rsStock.getInt("strawHen"), (rsStock.getInt("strawBuck") + rsStock.getInt("strawHen"))};
                dtm.addRow(tableRow);
            }
            if (Database.maxDateStock() != null) {
                maxDate = Database.maxDateStock();
                cal.setTime(maxDate);
                cal.add(Calendar.DATE, 1);
            } else {
                maxDate = arriveDate;
                cal.setTime(maxDate);
            }
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            stock = Database.selectStockByDate(cal.getTime());
            while (cal.getTime().before(calToday.getTime())) {
                Object[] tableRow = {SDFDATE.format(cal.getTime()), stock, 0, 0, 0, 0, 0};
                dtm.addRow(tableRow);
                cal.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A raktárkészlettel kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        ModelDatasForExcel.setDtmStock(dtm);
    }

    /**
     * A raktárkészlettel kapcsolatos adatok elmentése az adatbázisba.
     */
    public static void tableStockSave(DefaultTableModel dtm, Frame owner) {
        boolean ok = true;
        if (ok) {
            Database.deleteStock();
            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (Database.insertStock(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 4).toString(),
                        dtm.getValueAt(i, 5).toString(), dtm.getValueAt(i, 2).toString(),
                        dtm.getValueAt(i, 3).toString()))
                    ;
            }
            Database.updateStockNow();
            dtm.setRowCount(0);
            tableStockLoad(dtm, owner);
        }
    }

    /**
     * Az egyéb adatokkal kapcsolatos táblázat feltöltése a gyógyszerezésre,
     * technikai eseményekre, illetve az árváltozással kapcsolatos adatokkal.
     */
    public static void tableOtherLoad(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        Calendar cal = Calendar.getInstance();
        Date arriveDate = Database.selectArriveDate();
        try {
            cal.setTime(arriveDate);
            while (cal.getTime().before(calToday.getTime())) {
                Object[] tableRow = {SDFDATE.format(cal.getTime()), Database.selectMedicineByDate(cal.getTime()),
                    Database.selectTechEventByDate(cal.getTime()), Database.selectPriceChangeByDate(cal.getTime())};
                dtm.addRow(tableRow);
                cal.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Az egyéb adatokkal kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        ModelDatasForExcel.setDtmOther(dtm);
    }

    /**
     * Az egyéb adatokkal kapcsolatos adatok elmentése az adatbázisba.
     */
    public static void tableOtherSave(DefaultTableModel dtm, Frame owner) {
        Database.deleteMedicine();
        Database.deleteTechEvent();
        Database.deletePriceChange();
        boolean ok = true;
        for (int i = 0; i < dtm.getRowCount(); i++) {
            if (!dtm.getValueAt(i, 1).toString().equals("")) {
                if (Database.insertMedicine(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 1).toString()))
                    ; else {
                    ok = false;
                }
            }
            if (!dtm.getValueAt(i, 2).toString().equals("")) {
                if (Database.insertTechEvent(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 2).toString()))
                    ; else {
                    ok = false;
                }
            }
            if (!dtm.getValueAt(i, 3).toString().equals("")) {
                if (Database.insertPriceChange(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 3).toString()))
                    ; else {
                    ok = false;
                }
            }
        }
        if (ok) {
            dtm.setRowCount(0);
            tableOtherLoad(dtm, owner);
        } else {
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Nem minden adat elmentése sikerült! Töltse be újra a táblázatot!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * A heti méréseket tartalmazó táblázat betöltése.
     */
    public static void tableWeighingLoad(DefaultTableModel dtm, Frame owner) {
        dtm.setRowCount(0);
        Database.openConnection();
        ResultSet rsWeighing = Database.selectWeighing();
        Calendar calToday = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date arriveDate = null;
        Date maxDate = null;
        int week = 0;
        double buckWeightGain = 0;
        double henWeightGain = 0;
        double allWeightGain = 0;
        int buckNum = 0;
        int henNum = 0;
        int allNum = 0;
        try {
            arriveDate = Database.selectArriveDate();
            cal.setTime(arriveDate);
            while (rsWeighing.next()) {
                buckNum = Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[BUCK], BUCK);
                henNum = Database.selectTurkeyNumberByDate(cal.getTime(), TURKEY[HEN], HEN);
                allNum = buckNum + henNum;
                if (week > 0) {
                    buckWeightGain = rsWeighing.getDouble("buckWeight") - buckWeightGain;
                    henWeightGain = rsWeighing.getDouble("henWeight") - henWeightGain;
                    allWeightGain = rsWeighing.getDouble("allWeight") - allWeightGain;
                }
                Object[] tableRow = {rsWeighing.getString("week"), rsWeighing.getDouble("buckWeight"),
                    buckWeightGain, DoubleFormat.doubleFormat((buckNum * rsWeighing.getDouble("buckWeight"))),
                    DoubleFormat.doubleFormat((buckNum * buckWeightGain)), rsWeighing.getDouble("henWeight"),
                    henWeightGain, DoubleFormat.doubleFormat((henNum * rsWeighing.getDouble("henWeight"))),
                    DoubleFormat.doubleFormat((henNum * henWeightGain)), rsWeighing.getDouble("allWeight"),
                    allWeightGain, DoubleFormat.doubleFormat((allNum * rsWeighing.getDouble("allWeight"))),
                    DoubleFormat.doubleFormat((allNum * allWeightGain))};
                dtm.addRow(tableRow);
                buckWeightGain = rsWeighing.getDouble("buckWeight");
                henWeightGain = rsWeighing.getDouble("henWeight");
                allWeightGain = rsWeighing.getDouble("allWeight");
                cal.add(Calendar.DATE, 7);
                week++;
            }
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            calToday.add(Calendar.DATE, 3);
            while (cal.getTime().before(calToday.getTime())) {
                Object[] tableRow = {week, 0, null, null, null, 0, null, null, null, null, null, null, null};
                dtm.addRow(tableRow);
                cal.add(Calendar.DATE, 7);
                week++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A heti méréssel kapcsolatos adatok betöltése nem sikerült!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            Database.closeConnection();
        }
        Database.closeConnection();
        ModelDatasForExcel.setDtmWeighing(dtm);
    }

    /**
     * A heti mérésekkel kapcsolatos adatok elmentése az adatbázis megfelelő
     * táblájába.
     */
    public static void tableWeighingSave(DefaultTableModel dtm, Frame owner) {
        boolean ok = false;
        boolean success = true;
        int counter = 0;
        do {
            if (Double.parseDouble(dtm.getValueAt(counter, 1).toString()) != 0
                    && Double.parseDouble(dtm.getValueAt(counter, 5).toString()) != 0)
                ; else {
                success = false;
                JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A mérési adat biztosan több, mint 0!</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            }
            counter++;
        } while (success && counter < dtm.getRowCount());
        if (success) {
            counter = 1;
            double formerBuckWeight = Double.parseDouble(dtm.getValueAt(0, 1).toString());
            double formerHenWeight = Double.parseDouble(dtm.getValueAt(0, 5).toString());
            do {
                if (Double.parseDouble(dtm.getValueAt(counter, 1).toString()) >= formerBuckWeight
                        && Double.parseDouble(dtm.getValueAt(counter, 5).toString()) >= formerHenWeight) {
                    formerBuckWeight = Double.parseDouble(dtm.getValueAt(counter, 1).toString());
                    formerHenWeight = Double.parseDouble(dtm.getValueAt(counter, 5).toString());
                } else {
                    success = false;
                    JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>A mérési adat nem lehet kissebb, mint az előző heti adat!</b></font></html>",
                            "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                }
                counter++;
            } while (success && counter < dtm.getRowCount());
        }
        if (success) {
            ok = true;
            Database.deleteWeighing();
            Database.openConnection();
            ResultSet rs = Database.selectTurkeyArrive();
            int allBuck = 0;
            int allHen = 0;
            int buckNum = 0;
            int henNum = 0;
            double allWeightAvg = 0;
            Date arriveDate = null;
            Calendar cal = Calendar.getInstance();
            try {
                rs.next();
                arriveDate = rs.getDate("date");
                allBuck = rs.getInt("buck");
                allHen = rs.getInt("hen");
                cal.setTime(arriveDate);
                cal.add(Calendar.DATE, 6);
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    buckNum = allBuck - Database.allTurkeyLost(TURKEY[BUCK], cal.getTime());
                    henNum = allHen - Database.allTurkeyLost(TURKEY[HEN], cal.getTime());
                    allWeightAvg = ((Double.parseDouble(dtm.getValueAt(i, 1).toString()) * buckNum)
                            + (Double.parseDouble(dtm.getValueAt(i, 5).toString()) * henNum)) / (buckNum + henNum);
                    if (Database.insertWeighing(dtm.getValueAt(i, 0).toString(),
                            dtm.getValueAt(i, 1).toString(),
                            dtm.getValueAt(i, 5).toString(),
                            Double.toString(DoubleFormat.doubleFormat(allWeightAvg)))) {
                        cal.add(Calendar.DATE, 7);
                    } else {
                        ok = false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ok = false;
            }
        }
        if (ok) {
            dtm.setRowCount(0);
            tableWeighingLoad(dtm, owner);
        } else if (!ok && success) {
            JOptionPane.showMessageDialog(owner, "<html><font color=\"red\"><b>Nem minden adat elmentése sikerült! Töltse be újra a táblázatot!</b></font></html>",
                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
        }
        Database.closeConnection();
    }
}
