
package view;

import interfaces.ModelInterface;
import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.*;

/**
 * Ez a program főképernyője. A főképernyőn van hét fül, amelyeken van egy-egy
 * táblázat (JTable). A táblázatokból tudunk lekérdezni, és tudjuk szerkeszteni
 * azok tartalmát. A hét táblázat segítségével figyelemmel kísérhetjük egy
 * szezon egészét.
 */

public class ViewMainScreen extends JFrame implements ViewInterface, ActionListener {
    
        private JButton btBasicSelect = new JButton("Lekérdez");
        private JButton btConsBuckSelect = new JButton("Lekérdez");
        private JButton btConsHenSelect = new JButton("Lekérdez");
        private JButton btBuckHenSelect = new JButton("Lekérdez");
        private JButton btStockSelect = new JButton("Lekérdez ");
        private JButton btOtherSelect = new JButton("Lekérdez");
        private JButton btNewWeighing = new JButton("Átlagot számol");
        private JButton btStock = new JButton("Rendelés-kalkulátor");
        private JButton btOnStock = new JButton("Raktáron");
        private JButton btBasicSave = new JButton("Ment ");
        private JButton btConsBuckSave = new JButton("Ment");
        private JButton btConsHenSave = new JButton("Ment");
        private JButton btBuckHenUpdate = new JButton("Frissít");
        private JButton btStockSave = new JButton("Ment");
        private JButton btOtherSave = new JButton("Ment");
        private JButton btWeighingSave = new JButton("Ment");
        private JButton btChangeNumber = new JButton("Átminősít");
        
        private JMenuBar mb = new JMenuBar();        
        private JMenu mnFile = new JMenu("Fájl");
        private JMenu mnSeason = new JMenu("Szezon");
        private JMenu mnIndexNumbers = new JMenu("Fajlagos mutatószámok");
        private JMenu mnDatas = new JMenu("Adatok");
        private JMenu mnHelp = new JMenu("Súgó");
        private JMenuItem miLogin = new JMenuItem("Jelszó módosítása");
        private JMenuItem miClose = new JMenuItem("Kilép");        
        private JMenuItem miStartSeason = new JMenuItem("Szezon kezdete");
        private JMenuItem miEndSeason = new JMenuItem("Szezon befejezése");        
        private JMenuItem miIndNum = new JMenuItem("Fajlagos mutatószámok megtekintése");    
        private JMenuItem miConsQuan = new JMenuItem("Előírt fogyasztási mennyiségek");
        private JMenuItem miUnits = new JMenuItem("Mértékegységek");
        private JMenuItem miArriveDatas = new JMenuItem("Érkezési adtok");
        private JMenuItem miAbout = new JMenuItem("Névjegy");
        
        private JTabbedPane tp = new JTabbedPane();
        private JPanel pnNoSeason = new JPanel(new GridBagLayout());
        
        /*
         * A hét táblázat mögött lévő modelt felül kell definiálni. Meg
         * kell határozni, hogy mely cellák szerkeszthetőek, melyek milyen
         * adatokat tartalmaznak, valamint meg kell határozni annak
         * következményét, ha a felhasználó helytelen adatot gépel be.
         */
        private DefaultTableModel dtmBasic = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                switch (col) {
                    case 6: return true;
                    case 7: return true;
                    case 8: return true;
                    case 9: return true;
                    default: return false;
                }
            }
            
            @Override
            public Class getColumnClass(int col) {
                return Integer.class;
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                if (value instanceof Integer) {
                    Integer v = (Integer)value;
                    if (v >= 0)
                        super.setValueAt(value, row, col);
                    else {
                        super.setValueAt(0, row, col);
                    }
                }
            }
            
        };
        private DefaultTableModel dtmConsBuck = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 4)
                    return true;
                else if (col == 9 && (row+1)%7==0)
                    return true;
                else
                    return false;
            }
            
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 2: return Double.class;
                    case 3: return Double.class;
                    case 6: return Double.class;
                    case 7: return Double.class;
                    default: return Integer.class;
                }
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col==4) {
                    if (value instanceof Integer) {
                        Integer v = (Integer)value;
                        if (v >= 0)
                            super.setValueAt(value, row, col);
                        else {
                            super.setValueAt(0, row, col);
                        }
                    }
                } else if (col==9) {
                    if (value instanceof Integer) {
                        Integer v = (Integer)value;
                        if (v >= 0)
                            super.setValueAt(value, row, col);
                        else {
                            super.setValueAt(0, row, col);
                        }
                    }
                }                    
            }
        };
        private DefaultTableModel dtmConsHen = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 4)
                    return true;
                else if (col == 9 && (row+1)%7==0)
                    return true;
                else
                    return false;
            }
            
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 2: return Double.class;
                    case 3: return Double.class;
                    case 6: return Double.class;
                    case 7: return Double.class;
                    default: return Integer.class;
                }
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col==4) {
                    if (value instanceof Integer) {
                        Integer v = (Integer)value;
                        if (v >= 0)
                            super.setValueAt(value, row, col);
                        else {
                            super.setValueAt(0, row, col);
                        }
                    }
                } else if (col==9) {
                    if (value instanceof Integer) {
                        Integer v = (Integer)value;
                        if (v >= 0)
                            super.setValueAt(value, row, col);
                        else {
                            super.setValueAt(0, row, col);
                        }
                    }
                }                    
            }
        };
        private DefaultTableModel dtmBuckHen = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 6: return Double.class;
                    case 7: return Double.class;
                    default: return Integer.class;
                }
            }
        };
        private DefaultTableModel dtmStock = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                switch (col) {
                    case 2: return true;
                    case 3: return true;
                    case 4: return true;
                    case 5: return true;
                    default: return false;
                }
            }
            
            @Override
            public Class getColumnClass(int col) {
                return Integer.class;
            }
        };
        private DefaultTableModel dtmOther = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0)
                    return false;
                else
                    return true;
            }
            
            @Override
            public Class getColumnClass(int col) {
                return String.class;
            }
        };
        private DefaultTableModel dtmWeighing = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (row==0)
                    return false;
                else {
                    switch(col) {
                        case 1: return true;
                        case 5: return true;
                        default: return false;
                    }
                }
            }
            
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 0: return Integer.class;
                    case 1: return Double.class;
                    case 2: return Double.class;
                    case 3: return Integer.class;
                    case 4: return Integer.class;
                    case 5: return Double.class;
                    case 6: return Double.class;
                    case 7: return Integer.class;
                    case 8: return Integer.class;
                    case 9: return Double.class;
                    case 10: return Double.class;
                    case 11: return Integer.class;
                    case 12: return Integer.class;
                    default: return Integer.class;
                }
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col!=0) {
                    if (value instanceof Double) {
                        Double v = (Double)value;
                        int i=(int)(v*1000);
                        v=i/1000.0;
                        if (v>0)
                            super.setValueAt(v, row, col);
                        else
                            super.setValueAt(0, row, col);
                    }
                }
            }
        };
    
    public ViewMainScreen () {
        setMinimumSize(new Dimension(1024,600));
        setTitle("Pulyka");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((screen.getWidth()-this.getWidth())/2);
        int y = (int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x, y);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        mnFile.add(miLogin);
        mnFile.add(miClose);
        mnSeason.add(miStartSeason);
        mnSeason.add(miEndSeason);        
        mnIndexNumbers.add(miIndNum);     
        mnDatas.add(miConsQuan);
        mnDatas.add(miArriveDatas);
        mnDatas.add(miUnits);
        mnHelp.add(miAbout);
        mb.add(mnFile);
        mb.add(mnSeason);
        mb.add(mnIndexNumbers);
        mb.add(mnDatas);
        mb.add(mnHelp);
        setJMenuBar(mb);
        miLogin.addActionListener(this);
        miClose.addActionListener(this);
        miStartSeason.addActionListener(this);
        miEndSeason.addActionListener(this);
        miIndNum.addActionListener(this);
        miConsQuan.addActionListener(this);
        miArriveDatas.addActionListener(this);
        miUnits.addActionListener(this);
        miAbout.addActionListener(this);
        
        /*
         * A táblázatok létrehozása, az adatmodeljük beállítása.
         * A fejlécet a saját MultiLineHeaderRenderer osztály segítségével
         * formázzuk a megfelelő formára.
         */
        JTable tblBasic = new JTable();
        dtmBasic.setColumnIdentifiers(TURKEYBASIC);
        tblBasic.setModel(dtmBasic);
        Enumeration enumBasic = tblBasic.getColumnModel().getColumns();
        while (enumBasic.hasMoreElements()) {
            ((TableColumn)enumBasic.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JTable tblConsBuck = new JTable();
        dtmConsBuck.setColumnIdentifiers(TURKEYCONSUMPTION);
        tblConsBuck.setModel(dtmConsBuck);        
        Enumeration enumConsBuck = tblConsBuck.getColumnModel().getColumns();
        while (enumConsBuck.hasMoreElements()) {
            ((TableColumn)enumConsBuck.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JTable tblConsHen = new JTable();
        dtmConsHen.setColumnIdentifiers(TURKEYCONSUMPTION);
        tblConsHen.setModel(dtmConsHen);
        Enumeration enumConsHen = tblConsHen.getColumnModel().getColumns();
        while (enumConsHen.hasMoreElements()) {
            ((TableColumn)enumConsHen.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JTable tblBuckHen = new JTable();
        dtmBuckHen.setColumnIdentifiers(TURKEYBUCKHEN);
        tblBuckHen.setModel(dtmBuckHen);
        Enumeration enumBuckHen = tblBuckHen.getColumnModel().getColumns();
        while (enumBuckHen.hasMoreElements()) {
            ((TableColumn)enumBuckHen.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JTable tblStock = new JTable();
        dtmStock.setColumnIdentifiers(TURKEYSTOCK);
        tblStock.setModel(dtmStock);
        Enumeration enumStock = tblStock.getColumnModel().getColumns();
        while (enumStock.hasMoreElements()) {
            ((TableColumn)enumStock.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        for (int i=0; i<tblStock.getColumnCount(); i++)
            tblStock.getColumnModel().getColumn(i).sizeWidthToFit();
        
        JTable tblOther = new JTable();
        dtmOther.setColumnIdentifiers(TURKEYOTHER);
        tblOther.setModel(dtmOther);
        Enumeration enumOther = tblOther.getColumnModel().getColumns();
        while (enumOther.hasMoreElements()) {
            ((TableColumn)enumOther.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JTable tblWeighing = new JTable();
        dtmWeighing.setColumnIdentifiers(TURKEYWEIGHING);
        tblWeighing.setModel(dtmWeighing);
        Enumeration enumWeighing = tblWeighing.getColumnModel().getColumns();
        while (enumWeighing.hasMoreElements()) {
            ((TableColumn)enumWeighing.nextElement()).setHeaderRenderer(new MultiLineHeaderRenderer());
        }
        
        JScrollPane spBasic = new JScrollPane(tblBasic);
        JScrollPane spConsBuck = new JScrollPane(tblConsBuck);
        JScrollPane spConsHen = new JScrollPane(tblConsHen);
        JScrollPane spBuckHen = new JScrollPane(tblBuckHen);
        JScrollPane spStock = new JScrollPane(tblStock);
        JScrollPane spOther = new JScrollPane(tblOther);
        JScrollPane spWeighing = new JScrollPane(tblWeighing);
        
        JLabel lbBasic = new JLabel("Pulyák életkorának és létszámának nyilvántartása");
        JLabel lbConsBuck = new JLabel("A bakok fogyasztásával kapcsolatos adatok");
        JLabel lbConsHen = new JLabel("A tojók fogyasztásával kapcsolatos adatok");
        JLabel lbBuckHen = new JLabel("A bakok és tojók összesített adatai");
        JLabel lbStock = new JLabel("A raktárkészlettel kapcsolatos adatok");
        JLabel lbOther = new JLabel("Egyéb adatok nyilvántartása");
        JLabel lbWeighing = new JLabel("A heti mérések adatai");
        
        tp.addTab("Létszám", panelCreator(lbBasic, btBasicSave, btBasicSelect, spBasic));
        tp.addTab("Bakok fogyasztása", panelCreator(lbConsBuck, btConsBuckSave, btConsBuckSelect, spConsBuck));
        tp.addTab("Tojók fogyasztása", panelCreator(lbConsHen, btConsHenSave, btConsHenSelect, spConsHen));
        tp.addTab("Bakok és tojók", panelCreator(lbBuckHen, btBuckHenUpdate, btBuckHenSelect, spBuckHen));
        tp.addTab("Raktárkészlet", panelCreator(lbStock, btStockSave, btStockSelect, spStock));
        tp.addTab("Egyéb adatok", panelCreator(lbOther, btOtherSave, btOtherSelect, spOther));
        tp.addTab("Heti mérések", panelCreator(lbWeighing, btWeighingSave, btNewWeighing, spWeighing));
        
        JLabel lbNoSeason = new JLabel("Jelenleg nincs futó szezon.");
        lbNoSeason.setFont(new Font("Arial", Font.BOLD, 18));
        pnNoSeason.add(lbNoSeason);
        
        /*
         * Annak ellenőrzése, hogy jelenleg van-e futó szezon. Ez határozza meg,
         * hogy melyik JPanel-t rakjuk fel a főablakra.
         */
        try {
            if (ModelSeason.seasonOn) {
                add(tp);
                menuChange(true);
                tableAllLoad();
            } else {
                add(pnNoSeason);
                menuChange(false);
            }
        } catch (Exception e) {
            add(pnNoSeason);
            menuChange(false);
        }
        
        setVisible(true);
    }
    
    /**
     * A JTabbedPane-re az összes panel gyártása ezzel a metódussal
     * történik.
     */
    private JPanel panelCreator (JLabel lb, JButton btSave, JButton bt, JScrollPane sp) {
        lb.setFont(new Font("Arial", Font.BOLD, 18));
        lb.setHorizontalAlignment(JLabel.CENTER);
        JPanel pnLb = new JPanel(new FlowLayout());
        JPanel pnBt = new JPanel(new FlowLayout());
        JPanel pnUp = new JPanel(new BorderLayout(10, 10));
        pnLb.add(lb);
        pnBt.add(btSave);
        pnBt.add(bt);
        btSave.addActionListener(this);
        bt.addActionListener(this);
        if (btSave.getText().equals(btBasicSave.getText())) {
            pnBt.add(btChangeNumber);
            btChangeNumber.addActionListener(this);
        }
        if (bt.getText().equals(btStockSelect.getText())) {
            pnBt.add(btStock);
            pnBt.add(btOnStock);
            btStock.addActionListener(this);
            btOnStock.addActionListener(this);
        }
        pnUp.add(pnLb, BorderLayout.NORTH);
        pnUp.add(pnBt, BorderLayout.SOUTH);
        JPanel pn = new JPanel(new BorderLayout(10, 10));
        pn.add(pnUp, BorderLayout.NORTH);
        pn.add(sp, BorderLayout.CENTER);        
        return pn;
    }
    
    /**
     * Aszerint állítja be a menüket, hogy van-e futó szezon, vagy nincs.
     */
    private void menuChange(boolean seasonOn) {
        miStartSeason.setEnabled(!seasonOn);
        miEndSeason.setEnabled(seasonOn);
        miIndNum.setEnabled(seasonOn);
        miConsQuan.setEnabled(seasonOn);
        miUnits.setEnabled(seasonOn);
        miArriveDatas.setEnabled(seasonOn);
    }
    
    /**
     * A táblázatok betöltése.
     */
    private void tableAllLoad() {
        final ProgressBar pb = new ProgressBar();
        new Thread() {
        @Override
        public void run() {
            model.ModelTable.tableBasicLoad(dtmBasic, ViewMainScreen.this);
            ModelTable.tableConsumptionLoad(ModelInterface.TURKEY[ModelInterface.BUCK], dtmConsBuck, ViewMainScreen.this);
            ModelTable.tableConsumptionLoad(ModelInterface.TURKEY[ModelInterface.HEN], dtmConsHen, ViewMainScreen.this);
            ModelTable.tableBuckHenLoad(dtmBuckHen, ViewMainScreen.this);
            ModelTable.tableStockLoad(dtmStock, ViewMainScreen.this);
            ModelTable.tableOtherLoad(dtmOther, ViewMainScreen.this);
            ModelTable.tableWeighingLoad(dtmWeighing, ViewMainScreen.this);
            SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                pb.dispose();
                }
            }
            );
        }
        }.start();
        pb.setVisible(true);
    }

    /*
     * Eseménykezelés.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==miStartSeason) {
            ViewStart vs = new ViewStart();
            if (ModelSeason.seasonOn) {
                menuChange(true);
                dtmBasic.setRowCount(0);
                dtmConsBuck.setRowCount(0);
                dtmConsHen.setRowCount(0);
                dtmBuckHen.setRowCount(0);
                dtmStock.setRowCount(0);
                dtmOther.setRowCount(0);
                dtmWeighing.setRowCount(0);
                tableAllLoad();
                remove(pnNoSeason);
                add(tp);
                pack();
                revalidate();
            }
        } else if (e.getSource()==miEndSeason) {
            ViewFinish vf = new ViewFinish();
            if(!ModelSeason.isSeasonOn()) {
                menuChange(false);
                remove(tp);
                add(pnNoSeason);
                pack();
                revalidate();
            }
        } else if (e.getSource()==btBasicSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableBasicSave(dtmBasic, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btConsBuckSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableConsumptionSave(ModelInterface.TURKEY[ModelInterface.BUCK], dtmConsBuck, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btConsHenSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableConsumptionSave(ModelInterface.TURKEY[ModelInterface.HEN], dtmConsHen, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btBuckHenUpdate) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableBuckHenUpdate(dtmBuckHen, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btStockSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableStockSave(dtmStock, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btOtherSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableOtherSave(dtmOther, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btStock) {
            ModelStockCalculator msc = new ModelStockCalculator();
            JOptionPane.showMessageDialog(this, msc.getMessage(),
                        "Takarmányrendelés", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource()==btOnStock) {
            ModelOnStock.setOnStock();
            JOptionPane.showMessageDialog(this, "<html><font color=\"green\"><b>Jelenleg "+
                    ModelOnStock.getOnStock()+" kg takarmány van raktáron.</b></font></html>",
                        "Raktáron", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource()==btWeighingSave) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelTable.tableWeighingSave(dtmWeighing, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btNewWeighing) {
            ViewWeighingAverage vwa = new ViewWeighingAverage();
            if (ModelTurkeyWeight.getTw()!=null) {
                int column, row;
                if (ModelTurkeyWeight.getTw().getTurkey().equals("bak")) {
                    column = 1;
                } else {
                    column = 5;
                }
                row = ModelTurkeyWeight.getTw().getWeek();
                if (dtmWeighing.getRowCount()>row)
                    dtmWeighing.setValueAt(ModelTurkeyWeight.getTw().getWeighingAverage(), row, column);
                ModelTurkeyWeight.setTw(null);
            }
        } else if (e.getSource()==btBasicSelect) {
            ViewSelectBasic vsb = new ViewSelectBasic();
        } else if (e.getSource()==btConsBuckSelect) {
            ViewSelectBuckCons vsbc = new ViewSelectBuckCons();
        } else if (e.getSource()==btConsHenSelect) {
            ViewSelectHenCons vshc = new ViewSelectHenCons();
        } else if (e.getSource()==btBuckHenSelect) {
            ViewSelectBuckHen vsbh = new ViewSelectBuckHen();
        } else if (e.getSource()==btStockSelect) {
            ViewSelectStock vss = new ViewSelectStock();
        } else if (e.getSource()==btOtherSelect) {
            ViewSelectOther vso = new ViewSelectOther();
        } else if(e.getSource()==btChangeNumber) {
            ViewChangeTurkeyNumber vctn = new ViewChangeTurkeyNumber();
        } else if (e.getSource()==miClose) {
            System.exit(0);
        } else if (e.getSource()==miArriveDatas) {
            ViewArriveDatas vad = new ViewArriveDatas();
        } else if (e.getSource()==miConsQuan) {
            ViewConsumption vc = new ViewConsumption();
        } else if (e.getSource()==miUnits) {
            ViewUnit vu = new ViewUnit();
        } else if (e.getSource()==miIndNum) {
            ViewIndexNumbers vin = new ViewIndexNumbers();
        } else if (e.getSource()==miLogin) {
            ViewChangeLoginDatas vcld = new ViewChangeLoginDatas();
        } else if (e.getSource()==miAbout) {
            ViewAbout va = new ViewAbout();
        }
    }
    
}

/*
 * Források:
 * http://docs.oracle.com/javase/1.4.2/docs/api/javax/swing/table/DefaultTableModel.html
 * http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 * http://stackoverflow.com/questions/7531513/how-to-mark-jtable-cell-input-as-invalid
 * http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableFTFEditDemoProject/src/components/TableFTFEditDemo.java
 * http://prog.hu/tudastar/161838/Java+JTable.html
 * http://stackoverflow.com/questions/7792586/jtable-custom-header-renderer-and-sorting-icons
 * http://stackoverflow.com/questions/5270272/how-to-determine-day-of-week-by-passing-specific-date
 * http://www.mkyong.com/java/how-to-compare-dates-in-java/
 * http://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
 * http://stackoverflow.com/questions/4577792/how-to-clear-jtable
 * http://stackoverflow.com/questions/4216745/java-string-to-date-conversion
 * http://prog.hu/tudastar/164324/Dialogusablak+megjelenese.html
 */