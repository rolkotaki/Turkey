
package model;

import database.Database;
import interfaces.ModelInterface;
import interfaces.ViewInterface;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;

/**
 * A táblázatok kiírása Excel fájlba eme osztály segítségével történik. Minden
 * táblázat kiírását egy-egy osztálymedótus végzi. A táblázatok egyetlen
 * Excel fájl munkalapjaira lesznek exportálva.
 */
public class ModelExcel implements ModelInterface, ViewInterface {

    private static File exlFile;
    private static WritableWorkbook workbook;
    private static DateFormat customDateFormat = new DateFormat(DATEFORMAT);
    private static WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

    public static void exportAll(String path) {
        try {
            exlFile = new File(path+"/szezon_"+SDFDATE.format(Database.selectArriveDate()) + "_" +
                    SDFDATE.format(new Date())+"/tablazat.xls");
            workbook = Workbook.createWorkbook(exlFile);
            exportBasic(ModelDatasForExcel.getDtmBasic());
            exportConsumption(ModelDatasForExcel.getDtmBuckCons(), BUCK);
            exportConsumption(ModelDatasForExcel.getDtmHenCons(), HEN);
            exportBuckHen(ModelDatasForExcel.getDtmBuckHen());
            exportStock(ModelDatasForExcel.getDtmStock());
            exportOther(ModelDatasForExcel.getDtmOther());
            exportWeighing(ModelDatasForExcel.getDtmWeighing());
            exportIndexNumbers();
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A pulykák létszámával kapcsolatos adatok exportálása egy Excel
     * munkalapra.
     */
    private static void exportBasic(DefaultTableModel dtm) {
        try {
            WritableSheet sheet = workbook.createSheet(
                    "Létszám", 0);
            Label dt=new Label(0,0,TURKEYBASIC[0]);
            Label d=new Label(1,0,TURKEYBASIC[1]);
            Label a=new Label(2,0,TURKEYBASIC[2]);
            Label w=new Label(3,0,TURKEYBASIC[3]);
            Label b=new Label(4,0,TURKEYBASIC[4]);
            Label h=new Label(5,0,TURKEYBASIC[5]);
            Label db=new Label(6,0,TURKEYBASIC[6]);
            Label dh=new Label(7,0,TURKEYBASIC[7]);
            Label fb=new Label(8,0,TURKEYBASIC[8]);
            Label fh=new Label(9,0,TURKEYBASIC[9]);
            Label al=new Label(10,0,TURKEYBASIC[10]);
            
            sheet.addCell(dt);
            sheet.addCell(d);
            sheet.addCell(a);
            sheet.addCell(w);
            sheet.addCell(b);
            sheet.addCell(h);
            sheet.addCell(db);
            sheet.addCell(dh);
            sheet.addCell(fb);
            sheet.addCell(fh);
            sheet.addCell(al);
            
            DateTime date;
            Label day;
            Number age;
            Number week;
            Number buck;
            Number hen;
            Number deathBuck;
            Number deathHen;
            Number faultyBuck;
            Number faultyHen;
            Number allLoss;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                date = new DateTime(0, i+1, SDFDATE.parse(dtm.getValueAt(i, 0).toString()), dateFormat);
                day = new Label(1, i+1, dtm.getValueAt(i, 1).toString());
                age = new Number(2, i+1, (int)dtm.getValueAt(i, 2));
                week = new Number(3, i+1, (int)dtm.getValueAt(i, 3));
                buck = new Number(4, i+1, (int)dtm.getValueAt(i, 4));
                hen = new Number(5, i+1, (int)dtm.getValueAt(i, 5));
                deathBuck = new Number(6, i+1, (int)dtm.getValueAt(i, 6));
                deathHen = new Number(7, i+1, (int)dtm.getValueAt(i, 7));
                faultyBuck = new Number(8, i+1, (int)dtm.getValueAt(i, 8));
                faultyHen = new Number(9, i+1, (int)dtm.getValueAt(i, 9));
                allLoss = new Number(10, i+1, (int)dtm.getValueAt(i, 10));

                sheet.addCell(date);
                sheet.addCell(day);
                sheet.addCell(age);
                sheet.addCell(week);
                sheet.addCell(buck);
                sheet.addCell(hen);
                sheet.addCell(deathBuck);
                sheet.addCell(deathHen);
                sheet.addCell(faultyBuck);
                sheet.addCell(faultyHen);
                sheet.addCell(allLoss);
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
    
    /**
     * A bakok és tojók fogyasztásával kapcsolatos adatok exportálása egy
     * Excel munkalapra.
     */
    private static void exportConsumption(DefaultTableModel dtm, int turkey) {
        String s = "";
        switch(turkey) {
            case 0: s="Bak";break;
            case 1: s="Tojó";break;
        }
        try {
            WritableSheet sheet = workbook.createSheet(
                    s+" fogyasztás", 1);
            Label dt = new Label(0,0,TURKEYCONSUMPTION[0]);
            Label pw = new Label(1,0,TURKEYCONSUMPTION[1]);
            Label pd = new Label(2,0,TURKEYCONSUMPTION[2]);
            Label fb = new Label(3,0,TURKEYCONSUMPTION[3]);
            Label fk = new Label(4,0,TURKEYCONSUMPTION[4]);
            Label fa = new Label(5,0,TURKEYCONSUMPTION[5]);
            Label pda = new Label(6,0,TURKEYCONSUMPTION[6]);
            Label pds = new Label(7,0,TURKEYCONSUMPTION[7]);
            Label pwc = new Label(8,0,TURKEYCONSUMPTION[8]);
            Label rwc = new Label(9,0,TURKEYCONSUMPTION[9]);
            Label d = new Label(10,0,TURKEYCONSUMPTION[10]);
            Label pwcs = new Label(11,0,TURKEYCONSUMPTION[11]);
            Label rwcs = new Label(12,0,TURKEYCONSUMPTION[12]);
            Label ds = new Label(13,0,TURKEYCONSUMPTION[13]);
            
            sheet.addCell(dt);
            sheet.addCell(pw);
            sheet.addCell(pd);
            sheet.addCell(fb);
            sheet.addCell(fk);
            sheet.addCell(fa);
            sheet.addCell(pda);
            sheet.addCell(pds);
            sheet.addCell(pwc);
            sheet.addCell(rwc);
            sheet.addCell(d);
            sheet.addCell(pwcs);
            sheet.addCell(rwcs);
            sheet.addCell(ds);
            
            DateTime date;
            Number prescribedWeek;
            Number prescribedDay;
            Number fodderBag;
            Number fodderKg;
            Number fodderAll;
            Number prescribedDayAll;
            Number prescribedDaySum;
            Number preWeekCons;
            Number realWeekCons;
            Number difference;
            Number preWeekConsSum;
            Number realWeekConsSum;
            Number differenceSum;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                date = new DateTime(0, i+1, SDFDATE.parse(dtm.getValueAt(i, 0).toString()), dateFormat);
                prescribedWeek = new Number(1, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 1).toString())));
                prescribedDay = new Number(2, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 2).toString())));
                fodderBag = new Number(3, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 3).toString())));
                fodderKg = new Number(4, i+1, (int)dtm.getValueAt(i, 4));
                fodderAll = new Number(5, i+1, (int)dtm.getValueAt(i, 5));
                prescribedDayAll = new Number(6, i+1, (int)dtm.getValueAt(i, 6));
                prescribedDaySum = new Number(7, i+1, (int)dtm.getValueAt(i, 7));

                sheet.addCell(date);
                sheet.addCell(prescribedWeek);
                sheet.addCell(prescribedDay);
                sheet.addCell(fodderBag);
                sheet.addCell(fodderKg);
                sheet.addCell(fodderAll);
                sheet.addCell(prescribedDayAll);
                sheet.addCell(prescribedDaySum);
                
                if ((i+1)%7==0) {
                    preWeekCons = new Number(8, i+1, (int)dtm.getValueAt(i, 8));
                    realWeekCons = new Number(9, i+1, (int)dtm.getValueAt(i, 9));
                    difference = new Number(10, i+1, (int)dtm.getValueAt(i, 10));
                    preWeekConsSum = new Number(11, i+1, (int)dtm.getValueAt(i, 11));
                    realWeekConsSum = new Number(12, i+1, (int)dtm.getValueAt(i, 12));
                    differenceSum = new Number(13, i+1, (int)dtm.getValueAt(i, 13));
                    sheet.addCell(preWeekCons);
                    sheet.addCell(realWeekCons);
                    sheet.addCell(difference);
                    sheet.addCell(preWeekConsSum);
                    sheet.addCell(realWeekConsSum);
                    sheet.addCell(differenceSum);
                }                
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
    
    /**
     * A bakok és tojók közös adatainak exportálása egy Excel munkalapra.
     */
    private static void exportBuckHen(DefaultTableModel dtm) {
        try {
            WritableSheet sheet = workbook.createSheet(
                    "Bak és Tojó", 3);
            Label dt = new Label(0,0,TURKEYBUCKHEN[0]);
            Label n = new Label(1,0,TURKEYBUCKHEN[1]);
            Label l = new Label(2,0,TURKEYBUCKHEN[2]);
            Label al = new Label(3,0,TURKEYBUCKHEN[3]);
            Label f = new Label(4,0,TURKEYBUCKHEN[4]);
            Label fa = new Label(5,0,TURKEYBUCKHEN[5]);
            Label pd = new Label(6,0,TURKEYBUCKHEN[6]);
            Label pds = new Label(7,0,TURKEYBUCKHEN[7]);
            Label pwc = new Label(8,0,TURKEYBUCKHEN[8]);
            Label rwc = new Label(9,0,TURKEYBUCKHEN[9]);
            Label d = new Label(10,0,TURKEYBUCKHEN[10]);
            Label pwcs = new Label(11,0,TURKEYBUCKHEN[11]);
            Label rwcs = new Label(12,0,TURKEYBUCKHEN[12]);
            Label ds = new Label(13,0,TURKEYBUCKHEN[13]);
            
            sheet.addCell(dt);
            sheet.addCell(n);
            sheet.addCell(l);
            sheet.addCell(al);
            sheet.addCell(f);
            sheet.addCell(fa);
            sheet.addCell(pd);
            sheet.addCell(pds);
            sheet.addCell(pwc);
            sheet.addCell(rwc);
            sheet.addCell(d);
            sheet.addCell(pwcs);
            sheet.addCell(rwcs);
            sheet.addCell(ds);
            
            DateTime date;
            Number num;
            Number loss;
            Number allLoss;
            Number fodder;
            Number fodderAll;
            Number prescribedDay;
            Number prescribedDaySum;
            Number preWeekCons;
            Number realWeekCons;
            Number difference;
            Number preWeekConsSum;
            Number realWeekConsSum;
            Number differenceSum;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                date = new DateTime(0, i+1, SDFDATE.parse(dtm.getValueAt(i, 0).toString()), dateFormat);
                num = new Number(1, i+1, (int)dtm.getValueAt(i, 1));
                loss = new Number(2, i+1, (int)dtm.getValueAt(i, 2));
                allLoss = new Number(3, i+1, (int)dtm.getValueAt(i, 3));
                fodder = new Number(4, i+1, (int)dtm.getValueAt(i, 4));
                fodderAll = new Number(5, i+1, (int)dtm.getValueAt(i, 5));
                prescribedDay = new Number(6, i+1, (int)dtm.getValueAt(i, 6));
                prescribedDaySum = new Number(7, i+1, (int)dtm.getValueAt(i, 7));

                sheet.addCell(date);
                sheet.addCell(num);
                sheet.addCell(loss);
                sheet.addCell(allLoss);
                sheet.addCell(fodder);
                sheet.addCell(fodderAll);
                sheet.addCell(prescribedDay);
                sheet.addCell(prescribedDaySum);
                
                if ((i+1)%7==0) {
                    preWeekCons = new Number(8, i+1, (int)dtm.getValueAt(i, 8));
                    realWeekCons = new Number(9, i+1, (int)dtm.getValueAt(i, 9));
                    difference = new Number(10, i+1, (int)dtm.getValueAt(i, 10));
                    preWeekConsSum = new Number(11, i+1, (int)dtm.getValueAt(i, 11));
                    realWeekConsSum = new Number(12, i+1, (int)dtm.getValueAt(i, 12));
                    differenceSum = new Number(13, i+1, (int)dtm.getValueAt(i, 13));
                    sheet.addCell(preWeekCons);
                    sheet.addCell(realWeekCons);
                    sheet.addCell(difference);
                    sheet.addCell(preWeekConsSum);
                    sheet.addCell(realWeekConsSum);
                    sheet.addCell(differenceSum);
                }                
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
    
    /**
     * A raktárkészlettel kapcsolatos adatok exportálása egy Excel munkalapra.
     */
    private static void exportStock(DefaultTableModel dtm) {
        try {
            WritableSheet sheet = workbook.createSheet(
                    "Raktár", 4);
            Label dt = new Label(0,0,TURKEYSTOCK[0]);
            Label os = new Label(1,0,TURKEYSTOCK[1]);
            Label o = new Label(2,0,TURKEYSTOCK[2]);
            Label r = new Label(3,0,TURKEYSTOCK[3]);
            Label sb = new Label(4,0,TURKEYSTOCK[4]);
            Label sh = new Label(5,0,TURKEYSTOCK[5]);
            Label sa = new Label(6,0,TURKEYSTOCK[6]);
            
            sheet.addCell(dt);
            sheet.addCell(os);
            sheet.addCell(o);
            sheet.addCell(r);
            sheet.addCell(sb);
            sheet.addCell(sh);
            sheet.addCell(sa);
                        
            DateTime date;
            Number onStock;
            Number order;
            Number received;
            Number strawBuck;
            Number strawHen;
            Number strawAll;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                date = new DateTime(0, i+1, SDFDATE.parse(dtm.getValueAt(i, 0).toString()), dateFormat);
                onStock = new Number(1, i+1, (int)dtm.getValueAt(i, 1));
                order = new Number(2, i+1, (int)dtm.getValueAt(i, 2));
                received = new Number(3, i+1, (int)dtm.getValueAt(i, 3));
                strawBuck = new Number(4, i+1, (int)dtm.getValueAt(i, 4));
                strawHen = new Number(5, i+1, (int)dtm.getValueAt(i, 5));
                strawAll = new Number(6, i+1, (int)dtm.getValueAt(i, 6));

                sheet.addCell(date);
                sheet.addCell(onStock);
                sheet.addCell(order);
                sheet.addCell(received);
                sheet.addCell(strawBuck);
                sheet.addCell(strawHen);
                sheet.addCell(strawAll);
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
    
    /**
     * Egy egyéb adatokkal (gyógyszer, technikai események, árváltozás)
     * kapcsolatos adatok exportálása egy Excel munkalapra.
     */
    private static void exportOther(DefaultTableModel dtm) {
        try {
            WritableSheet sheet = workbook.createSheet(
                    "Egyéb", 5);
            Label dt = new Label(0,0,TURKEYOTHER[0]);
            Label m = new Label(1,0,TURKEYOTHER[1]);
            Label te = new Label(2,0,TURKEYOTHER[2]);
            Label pc = new Label(3,0,TURKEYOTHER[3]);
            
            sheet.addCell(dt);
            sheet.addCell(m);
            sheet.addCell(te);
            sheet.addCell(pc);
                        
            DateTime date;
            Label medicine;
            Label technEvent;
            Label priceChange;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                date = new DateTime(0, i+1, SDFDATE.parse(dtm.getValueAt(i, 0).toString()), dateFormat);
                medicine = new Label(1, i+1, dtm.getValueAt(i, 1).toString());
                technEvent = new Label(2, i+1, dtm.getValueAt(i, 2).toString());
                priceChange = new Label(3, i+1, dtm.getValueAt(i, 3).toString());

                sheet.addCell(date);
                sheet.addCell(medicine);
                sheet.addCell(technEvent);
                sheet.addCell(priceChange);
            }
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }
    
    /**
     * A heti méréssel kapcsolatos adatok exportálása egy Excel munkalapra.
     */
    private static void exportWeighing(DefaultTableModel dtm) {
        try {
            WritableSheet sheet = workbook.createSheet(
                    "Mérés", 6);
            Label w = new Label(0,0,TURKEYWEIGHING[0]);
            Label b = new Label(1,0,TURKEYWEIGHING[1]);
            Label bg = new Label(2,0,TURKEYWEIGHING[2]);
            Label ba = new Label(3,0,TURKEYWEIGHING[3]);
            Label bag = new Label(4,0,TURKEYWEIGHING[4]);
            Label h = new Label(5,0,TURKEYWEIGHING[5]);
            Label hg = new Label(6,0,TURKEYWEIGHING[6]);
            Label ha = new Label(7,0,TURKEYWEIGHING[7]);
            Label hag = new Label(8,0,TURKEYWEIGHING[8]);
            Label a = new Label(9,0,TURKEYWEIGHING[9]);
            Label ag = new Label(10,0,TURKEYWEIGHING[10]);
            Label as = new Label(11,0,TURKEYWEIGHING[11]);
            Label ags = new Label(12,0,TURKEYWEIGHING[12]);
            
            sheet.addCell(w);
            sheet.addCell(b);
            sheet.addCell(bg);
            sheet.addCell(ba);
            sheet.addCell(bag);
            sheet.addCell(h);
            sheet.addCell(hg);
            sheet.addCell(ha);
            sheet.addCell(hag);
            sheet.addCell(a);
            sheet.addCell(ag);
            sheet.addCell(as);
            sheet.addCell(ags);
            
            Number week;
            Number buck;
            Number bWG;
            Number buckAll;
            Number buckAllWG;
            Number hen;
            Number hWG;
            Number henAll;
            Number henAllWG;
            Number all;
            Number aWG;
            Number allSum;
            Number allSumWG;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                week = new Number(0, i+1, Integer.parseInt(dtm.getValueAt(i, 0).toString()));
                buck = new Number(1, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 1).toString())));
                bWG = new Number(2, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 2).toString())));
                buckAll = new Number(3, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 3).toString())));
                buckAllWG = new Number(4, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 4).toString())));
                hen = new Number(5, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 5).toString())));
                hWG = new Number(6, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 6).toString())));
                henAll = new Number(7, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 7).toString())));
                henAllWG = new Number(8, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 8).toString())));
                all = new Number(9, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 9).toString())));
                aWG = new Number(10, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 10).toString())));
                allSum = new Number(11, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 11).toString())));
                allSumWG = new Number(12, i+1,
                        DoubleFormat.doubleFormat(Double.parseDouble(dtm.getValueAt(i, 12).toString())));

                sheet.addCell(week);
                sheet.addCell(buck);
                sheet.addCell(bWG);
                sheet.addCell(buckAll);
                sheet.addCell(buckAllWG);
                sheet.addCell(hen);
                sheet.addCell(hWG);
                sheet.addCell(henAll);
                sheet.addCell(henAllWG);
                sheet.addCell(all);
                sheet.addCell(aWG);
                sheet.addCell(allSum);
                sheet.addCell(allSumWG);
            }
        } catch (Exception e) {
        }
    }
    
    private static void exportIndexNumbers() {
        DefaultTableModel dtmWeek = new DefaultTableModel();
        DefaultTableModel dtmCum = new DefaultTableModel();
        dtmWeek.setColumnIdentifiers(ViewInterface.INDEXNUMBER);
        dtmCum.setColumnIdentifiers(ViewInterface.INDEXNUMBER);
        try {
            ModelIndexNumber.tableWeekLoad(dtmWeek, null);
            ModelIndexNumber.tableCumLoad(dtmCum, null);
            WritableSheet sheet = workbook.createSheet(
                    "Mutatószámok", 7);
            Label w = new Label(0,0,"Hét\n(heti)");
            Label b = new Label(1,0,"Bak\n(heti)");
            Label h = new Label(2,0,"Tojó\n(heti)");
            Label c = new Label(3,0,"Együttes\n(heti)");
            Label w2 = new Label(5,0,"Hét\n(összesített)");
            Label b2 = new Label(6,0,"Bak\n(összesített)");
            Label h2 = new Label(7,0,"Tojó\n(összesített)");
            Label c2 = new Label(8,0,"Együttes\n(összesített)");
            
            sheet.addCell(w);
            sheet.addCell(b);
            sheet.addCell(h);
            sheet.addCell(c);
            sheet.addCell(w2);
            sheet.addCell(b2);
            sheet.addCell(h2);
            sheet.addCell(c2);
            
            Number week;
            Number buck;
            Number hen;
            Number all;
            for (int i = 0; i < dtmWeek.getRowCount(); i++) {
                week=new Number(0, i+1, Integer.parseInt(dtmWeek.getValueAt(i, 0).toString()));
                buck = new Number(1, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmWeek.getValueAt(i, 1).toString())));
                hen = new Number(2, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmWeek.getValueAt(i, 2).toString())));
                all = new Number(3, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmWeek.getValueAt(i, 3).toString())));

                sheet.addCell(week);
                sheet.addCell(buck);
                sheet.addCell(hen);
                sheet.addCell(all);
            }
            for (int i = 0; i < dtmCum.getRowCount(); i++) {
                week=new Number(5,i+1,Integer.parseInt(dtmCum.getValueAt(i, 0).toString()));
                buck = new Number(6, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmCum.getValueAt(i, 1).toString())));
                hen = new Number(7, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmCum.getValueAt(i, 2).toString())));
                all = new Number(8, i+1, DoubleFormat.doubleFormat(
                        Double.parseDouble(dtmCum.getValueAt(i, 3).toString())));

                sheet.addCell(week);
                sheet.addCell(buck);
                sheet.addCell(hen);
                sheet.addCell(all);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}

/*
 * Forrás: http://www.quicklyjava.com/write-to-excel-in-java/
 * http://www.quicklyjava.com/jexcel-jar-download/
 */
