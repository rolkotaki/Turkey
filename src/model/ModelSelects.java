
package model;

import database.Database;
import interfaces.ModelInterface;
import interfaces.ViewInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import view.CellRenderer;
import view.MultiLineHeaderRenderer;

/**
 * Az osztály a különböző lekérdezések eredménytáblázatainak betöltését végző
 * metódusokat tartalmazza.
 */
public class ModelSelects implements ModelInterface, ViewInterface {
    
    /**
     * A pulykák összes elhullásának lekérdezése egy adott időtartam alatt,
     * napi szinten.
     */
    public static void tblLoadLostAll(String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyLostPeriod(from, to);
        String[] columns = {"Dátum","Bak halál","Tojó halál","Selejt bak","Selejt tojó"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getString("Dátum"), rs.getInt("Halál_bak"),
                    rs.getInt("Halál_tojó"), rs.getInt("Selejt_bak"), rs.getInt("Selejt_tojó")};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
    /**
     * A pulykák elhullásának lekérdezése. Itt azonban a felhasználó
     * választja ki, hogy a bakok vagy tojók elhalálozását, illetve selejtezését
     * szeretné lekérdezni. Legalább egy szempontot választani kell, de akár
     * az összeset is választhatja.
     */
    public static void tblLoadLostFunction(String function, Vector v, Vector col, String from, String to,
            JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyLostPeriodFunction(function, from, to, v);
        try {
            dtm.setRowCount(0);
            dtm.setColumnIdentifiers(col);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            tbl.getColumnModel().getColumn(0).setPreferredWidth(150);
            String period = from + " - " + to;
            Vector rowData=new Vector();
            rowData.addElement(period);
            rs.next();
            for(int i=0; i<v.size(); i++) {
                rowData.addElement(rs.getDouble(i+1));
            }
            dtm.addRow(rowData);
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
    /**
     * A bakok/tojók számára kivitt takarmány mennyiségének lekérdezése egy
     * adott időintervallum alatt, napi szinten.
     */
    public static void tblLoadFodderAll(String turkey, String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectFodderPeriod(turkey, from, to);
        String[] columns = {"Dátum","Takarmánykivitel (kg)"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getString("Dátum"), rs.getInt("Takarmány_kg")};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }

    /**
     * Egy adott időintervallum alatt a bakok/tojók számára kivitt takarmány
     * summázása, átlagolása, illetva a minimum vagy maximum érték lekérdezése.
     */
    public static void tblLoadFodderFunction(int index, String turkey, String from, String to, JTable tbl, DefaultTableModel dtm) {
        int result = Database.selectFodderPeriodFunction(SQLFUNCTION[index-1], turkey, from, to);
        String[] colId = {"Időtartam", COL_IDENTIFIERS[index - 1]};
        dtm.setColumnIdentifiers(colId);
        Enumeration en = tbl.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = (TableColumn) en.nextElement();
            tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
            tc.setCellRenderer(new CellRenderer());
        }
        dtm.setRowCount(0);
        String period = from + " - " + to;
        Object[] tableRow = {period, result};
        dtm.addRow(tableRow);
    }

    /**
     * A bakok/tojók heti valós fogyasztásainak lekérdezése.
     */
    public static void tblLoadConsumptionAll(String turkey, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectConsumptionRealAll(turkey);
        String[] columns = {"Hét", "Fogyasztás (kg)"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getInt("week"), rs.getInt("cons")};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }

    /**
     * A bakok/tojók heti valós fogyasztásainak summázása, átlagolása, illetve a
     * minimum vagy maximum érték lekérdezése.
     */
    public static void tblLoadConsumptionFunction(int index, String turkey, JTable tbl, DefaultTableModel dtm) {
        int result = Database.selectConsRealFunction(SQLFUNCTION[index - 1], turkey);
        String[] colId = {COL_IDENTIFIERS[index - 1]};
        dtm.setColumnIdentifiers(colId);
        Enumeration en = tbl.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = (TableColumn) en.nextElement();
            tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
            tc.setCellRenderer(new CellRenderer());
        }
        dtm.setRowCount(0);
        Object[] tableRow = {result};
        dtm.addRow(tableRow);
    }
    
    /**
     * A bakok és tojók számára kivitt takarmány mennyiségének lekérdezése
     * napi szinten summázva, egy adott időintervallum alatt.
     */
    public static void tblLoadCumFodderAll(String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectFodderBothPeriod(from, to);
        String[] columns = {"Dátum","Takarmánykivitel (kg)"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getString("Dátum"), rs.getInt("Takarmány_kg")};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }

    /**
     * A bakok és tojók számára kivitt takarmány mennyiségének lekérdezése
     * egy adott időintervallum alatt, majd ezen értékek summázása, áltagolása,
     * illetve a minimum vagy maximum érték lekérdezése.
     */
    public static void tblLoadCumFodderFunction(int index, String from, String to, JTable tbl,
                    DefaultTableModel dtm) {
        int result = Database.selectFodderBothFunction(SQLFUNCTION[index - 1], from, to);
        String[] colId = {"Időtartam", COL_IDENTIFIERS[index - 1]};
        dtm.setColumnIdentifiers(colId);
        Enumeration en = tbl.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = (TableColumn) en.nextElement();
            tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
            tc.setCellRenderer(new CellRenderer());
        }
        dtm.setRowCount(0);
        String period = from + " - " + to;
        Object[] tableRow = {period, result};
        dtm.addRow(tableRow);
    }

    /**
     * A bakok és tojók heti valós fogyasztásainak lekérdezése, heti szinten
     * summázva.
     */
    public static void tblLoadCumConsumptionAll(JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectConsRealBoth();
        String[] columns = {"Hét", "Fogyasztás (kg)"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getInt("week"), rs.getInt("cons")};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }

    /**
     * A bakok és tojók fogyasztásainak lekérdezése heti szinten summázva,
     * majd ezen adatok summázása, átlagolása, illetve a minimum vagy maximum
     * érték lekérdezése.
     */
    public static void tblLoadCumConsumptionFunction(int index, JTable tbl, DefaultTableModel dtm) {
        int result = Database.selectConsRealBothFunction(SQLFUNCTION[index-1]);
        String[] colId = {COL_IDENTIFIERS[index - 1]};
        dtm.setColumnIdentifiers(colId);
        Enumeration en = tbl.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = (TableColumn) en.nextElement();
            tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
            tc.setCellRenderer(new CellRenderer());
        }
        dtm.setRowCount(0);
        Object[] tableRow = {result};
        dtm.addRow(tableRow);
    }
    
    /**
     * A bakok és tojók elhullásának lekérdezése, napi szinten összeadva.
     */
    public static void tblLoadCumLostAll(String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyLostPeriod(from, to);
        String[] columns = {"Dátum","Napi elhullás"};
        try {
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            dtm.setRowCount(0);
            while (rs.next()) {
                Object[] tableRow = {rs.getString("Dátum"), (rs.getInt("Halál_bak")+rs.getInt("Halál_tojó")+
                        rs.getInt("Selejt_bak")+rs.getInt("Selejt_tojó"))};
                dtm.addRow(tableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
    /**
     * A pulykák elhullásának lekérdezése napi szinten summázva, majd ezen
     * adatok summázása, átlagolása, illetve a minimum vagy maximum érték
     * lekérdezése.
     */
    public static void tblLoadCumLostFunction(int index, String from, String to, JTable tbl,
                    DefaultTableModel dtm) {
        int result = Database.selectTurkeyLostBothFunction(SQLFUNCTION[index-1], from, to);
        String[] colId = {COL_IDENTIFIERS[index - 1]};
        dtm.setColumnIdentifiers(colId);
        Enumeration en = tbl.getColumnModel().getColumns();
        while (en.hasMoreElements()) {
            TableColumn tc = (TableColumn) en.nextElement();
            tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
            tc.setCellRenderer(new CellRenderer());
        }
        dtm.setRowCount(0);
        Object[] tableRow = {result};
        dtm.addRow(tableRow);
    }
    
    /**
     * A raktár táblázat betöltése, egy adott időintervallum határain belül.
     */
    public static void tblLoadSelectStockAll(String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs=Database.selectStockPeriod(from, to);
        String[] columns = {"Dátum","Takarmány-\nrendelés","Takarmány-\nfogadás","Szalmabála"};
        try {
            dtm.setRowCount(0);
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            while(rs.next()) {
                Object[] tableRow = {rs.getString("date"), rs.getInt("fOrder"),
                    rs.getInt("fReceive"),rs.getInt("straw")};
                dtm.addRow(tableRow);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        
        Database.closeConnection();
    }
    
    /**
     * A raktár adatainak lekérdezése egy adott időintervallumban, majd ezen
     * adatok summázása, átlagolása, vagy a minimum illete maximum érték
     * lekérdezése. CheckBox-ok segítségével a lekérdezés történhet egy konkrét
     * dologra, vagy akár az összesre is.
     */
    public static void tblLoadStockFunction(int index, String from, String to, Vector v, Vector col,
                    JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs = Database.selectStockFunction(SQLFUNCTION[index - 1], v, from, to);
        try {
            dtm.setRowCount(0);
            dtm.setColumnIdentifiers(col);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            tbl.getColumnModel().getColumn(0).setPreferredWidth(130);
            String period = from + " - " + to;
            Vector rowData=new Vector();
            rowData.addElement(period);
            rs.next();
            for(int i=0; i<v.size(); i++) {
                rowData.addElement(DoubleFormat.doubleFormat(rs.getDouble(i+1)));
            }
            dtm.addRow(rowData);
        } catch (SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }
        Database.closeConnection();
    }
    
    /**
     * A gyógyszerezéssel, technikai eseményekkel, valamint az árváltozással
     * kapcsolatos adatok lekérdezése egy adott időszakon belül. A felhasználó
     * ezen felül rákereshet bizonyos szövegrészletekre, kulcsszavakra is,
     * amelyet egy szövegmezőben kell megadnia.
     */
    public static void tblLoadOther(int index, String text,
            String from, String to, JTable tbl, DefaultTableModel dtm) {
        Database.openConnection();
        ResultSet rs=null;
        String[] columns=null;
        dtm.setRowCount(0);
        switch (index) {
            case 0:
                rs=Database.selectMedicineByDateText(text, from, to);
                columns=new String[]{"Dátum","Gyógyszer"};
                break;
            case 1: rs=Database.selectTechEventByDateText(text, from, to);
                columns=new String[]{"Dátum","Technikai\nesemény"};
                break;
            case 2: rs=Database.selectPriceChangeByDateText(text, from, to);
                columns=new String[]{"Dátum","Árváltozás"};
                break;
        }
        try {         
            dtm.setColumnIdentifiers(columns);
            Enumeration en = tbl.getColumnModel().getColumns();
            while (en.hasMoreElements()) {
                TableColumn tc = (TableColumn) en.nextElement();
                tc.setHeaderRenderer(new MultiLineHeaderRenderer());                
                tc.setCellRenderer(new CellRenderer());
            }
            while(rs.next()) {
                Object[] tableRow = {rs.getString("date"), rs.getString("description")};
                dtm.addRow(tableRow);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            Database.closeConnection();
        }        
        Database.closeConnection();
    }
    
}
