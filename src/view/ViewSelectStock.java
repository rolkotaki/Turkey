
package view;

import com.toedter.calendar.JDateChooser;
import interfaces.ModelInterface;
import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.ModelSelects;

/**
 * Ez az ablak a raktárkészlet adatainak lekérdezésére szolgál.
 */

public class ViewSelectStock extends JDialog implements ModelInterface, ActionListener, WindowListener {
    
    private JTable tbl;
    private JDateChooser dcFrom = new JDateChooser(new Date(), DATEFORMAT);
    private JDateChooser dcUntil = new JDateChooser(new Date(), DATEFORMAT);
    private JComboBox cb = new JComboBox(ViewInterface.CBLIST);
    private JCheckBox cbOrder = new JCheckBox("takarmányrendelés");
    private JCheckBox cbRecieve = new JCheckBox("takarmányfogadás");
    private JCheckBox cbHay = new JCheckBox("szalmabála");
    private JButton btSelect = new JButton("Lekérdez");
    private DefaultTableModel dtm = new DefaultTableModel() {
        @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
    };    
    
    public ViewSelectStock () {
        setMinimumSize(new Dimension(500, 500));
        setTitle("Lekérdezés");
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new GridLayout(4, 1));
        JPanel pnTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JPanel pnDateCombo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JPanel pnRadioButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel pnTable = new JPanel(new BorderLayout());
        
        JLabel lbTitle = new JLabel("Raktárral kapcsolatos adatok lekérdezése");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 16));
        pnTitle.add(lbTitle);
        
        dcFrom.setPreferredSize(new Dimension(95,20));
        dcUntil.setPreferredSize(new Dimension(95,20));
        pnDateCombo.add(new JLabel("Időszak:"));
        pnDateCombo.add(dcFrom);
        pnDateCombo.add(new JLabel("-tól"));
        pnDateCombo.add(dcUntil);
        pnDateCombo.add(new JLabel("-ig               "));
        pnDateCombo.add(cb);
        cb.addActionListener(this);
        
        pnRadioButtons.add(cbOrder);
        pnRadioButtons.add(cbRecieve);
        pnRadioButtons.add(cbHay);
        cbOrder.addActionListener(this);
        cbRecieve.addActionListener(this);
        cbHay.addActionListener(this);
        cbOrder.setEnabled(false);cbRecieve.setEnabled(false);cbHay.setEnabled(false);
        
        pnButton.add(btSelect);
        btSelect.addActionListener(this);
        
        tbl = new JTable(dtm);
        JScrollPane spTable = new JScrollPane(tbl);
        pnTable.add(spTable);
        
        pnNorth.add(pnTitle);
        pnNorth.add(pnDateCombo);
        pnNorth.add(pnRadioButtons);
        pnNorth.add(pnButton);
        add(pnNorth, BorderLayout.NORTH);
        add(spTable, BorderLayout.CENTER);
        
        addWindowListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==cb) {
            switch (cb.getSelectedIndex()) {
                case 0: cbOrder.setEnabled(false);cbRecieve.setEnabled(false);cbHay.setEnabled(false);break;
                case 1: cbOrder.setEnabled(true);cbRecieve.setEnabled(true);cbHay.setEnabled(true);break;
                case 2: cbOrder.setEnabled(true);cbRecieve.setEnabled(true);cbHay.setEnabled(true);break;
                case 3: cbOrder.setEnabled(true);cbRecieve.setEnabled(true);cbHay.setEnabled(true);break;
                case 4: cbOrder.setEnabled(true);cbRecieve.setEnabled(true);cbHay.setEnabled(true);break;
            }
        } else if (e.getSource()==btSelect) {
            try {
                if(SDFDATE.parse(SDFDATE.format(dcFrom.getDate())).
                        after(SDFDATE.parse(SDFDATE.format(dcUntil.getDate())))) {
                    JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Az első dátum nem lehet később a másodiknál!</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                } else {
                    if(cb.getSelectedIndex()==0) {
                        ModelSelects.tblLoadSelectStockAll(SDFDATE.format(dcFrom.getDate()),
                                SDFDATE.format(dcUntil.getDate()), tbl, dtm);
                    } else {
                        Vector<String> v = new Vector<String>();
                        Vector<String> col = new Vector<String>();
                        col.add("Időtartam");
                        if (cbOrder.isSelected()) {
                            v.add("fodder_order");
                            col.add("Takarmány-\nrendelés");
                        }
                        if (cbRecieve.isSelected()) {
                            v.add("fodder_receive");
                            col.add("Takarmány-\nfogadás");
                        }
                        if (cbHay.isSelected()) {
                            v.add("straw_buck + straw_hen");
                            col.add("Szalmabála");
                        }
                        if (!v.isEmpty())
                            ModelSelects.tblLoadStockFunction(cb.getSelectedIndex(),
                                    SDFDATE.format(dcFrom.getDate()),
                                SDFDATE.format(dcUntil.getDate()), v, col, tbl, dtm);
                        else
                            JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Nincs kiválasztva semmi!</b></font></html>",
                                    "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    
}

/*
 * Forrás: http://www.toedter.com/en/jcalendar/
 */
