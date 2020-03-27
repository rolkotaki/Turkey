
package model;

import javax.swing.table.DefaultTableModel;

/**
 * A táblázatok Excel tábázatba történő exportálásához szükséges tudni a
 * táblázatok modeljének tartalmát. Az alábbi osztály ezeket tartalmazza.
 */
public class ModelDatasForExcel {
    
    private static DefaultTableModel dtmBasic=null;
    private static DefaultTableModel dtmBuckCons=null;
    private static DefaultTableModel dtmHenCons=null;
    private static DefaultTableModel dtmBuckHen=null;
    private static DefaultTableModel dtmStock=null;
    private static DefaultTableModel dtmOther=null;
    private static DefaultTableModel dtmWeighing=null;

    public static DefaultTableModel getDtmBasic() {
        return dtmBasic;
    }

    public static void setDtmBasic(DefaultTableModel dtmBasic) {
        ModelDatasForExcel.dtmBasic = dtmBasic;
    }

    public static DefaultTableModel getDtmBuckCons() {
        return dtmBuckCons;
    }

    public static void setDtmBuckCons(DefaultTableModel dtmBuckCons) {
        ModelDatasForExcel.dtmBuckCons = dtmBuckCons;
    }

    public static DefaultTableModel getDtmBuckHen() {
        return dtmBuckHen;
    }

    public static void setDtmBuckHen(DefaultTableModel dtmBuckHen) {
        ModelDatasForExcel.dtmBuckHen = dtmBuckHen;
    }

    public static DefaultTableModel getDtmHenCons() {
        return dtmHenCons;
    }

    public static void setDtmHenCons(DefaultTableModel dtmHenCons) {
        ModelDatasForExcel.dtmHenCons = dtmHenCons;
    }

    public static DefaultTableModel getDtmOther() {
        return dtmOther;
    }

    public static void setDtmOther(DefaultTableModel dtmOther) {
        ModelDatasForExcel.dtmOther = dtmOther;
    }

    public static DefaultTableModel getDtmStock() {
        return dtmStock;
    }

    public static void setDtmStock(DefaultTableModel dtmStock) {
        ModelDatasForExcel.dtmStock = dtmStock;
    }

    public static DefaultTableModel getDtmWeighing() {
        return dtmWeighing;
    }

    public static void setDtmWeighing(DefaultTableModel dtmWeighing) {
        ModelDatasForExcel.dtmWeighing = dtmWeighing;
    }
       
}
