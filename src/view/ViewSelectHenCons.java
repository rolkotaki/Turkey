
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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.ModelSelects;

/**
 * Ez az ablak a tojók fogyasztásával kapcsolatos adatok lekérdezésére szolgál.
 * Lekérdezhetünk dátum, takarmány kivitel, valamint a fogyasztás alapján is.
 * A lekérdezésben az előbbi adatokat summázhatjuk, átlagolhatjuk, és
 * lekérdezhetjük a minimum, illetve a maximum adatokat is.
 */

public class ViewSelectHenCons extends JDialog implements ModelInterface, ActionListener, WindowListener {
    
    private JTable tbl;
    private JDateChooser dcFrom = new JDateChooser(new Date(), DATEFORMAT);
    private JDateChooser dcUntil = new JDateChooser(new Date(), DATEFORMAT);
    private JComboBox cb = new JComboBox(ViewInterface.CBLIST);
    private JRadioButton rbFodder = new JRadioButton("takarmány kivitel");
    private JRadioButton rbCons = new JRadioButton("heti fogyasztás");
    private JButton btSelect = new JButton("Lekérdez");
    private DefaultTableModel dtm = new DefaultTableModel() {
        @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
    };    
    
    public ViewSelectHenCons () {
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
        
        JLabel lbTitle = new JLabel("Tojók fogyasztásával kapcsolatos adatok lekérdezése");
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
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbFodder);
        bg.add(rbCons);
        rbFodder.doClick();
        pnRadioButtons.add(rbFodder);
        pnRadioButtons.add(rbCons);
        rbFodder.addActionListener(this);
        rbCons.addActionListener(this);
        
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
        if (e.getSource() == btSelect) {
            try {
                boolean ok = true;
                if (rbFodder.isSelected()) {
                    if (SDFDATE.parse(SDFDATE.format(dcFrom.getDate())).
                            after(SDFDATE.parse(SDFDATE.format(dcUntil.getDate())))) {
                        JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Az első dátum nem lehet később a másodiknál!</b></font></html>",
                                "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                        ok = false;
                    }
                    if (ok) {
                        if (cb.getSelectedIndex() == 0) {
                            ModelSelects.tblLoadFodderAll(TURKEY[HEN], SDFDATE.format(dcFrom.getDate()),
                                SDFDATE.format(dcUntil.getDate()), tbl, dtm);
                        } else {
                            ModelSelects.tblLoadFodderFunction(cb.getSelectedIndex(), TURKEY[HEN],
                                    SDFDATE.format(dcFrom.getDate()),
                                SDFDATE.format(dcUntil.getDate()), tbl, dtm);
                        }
                    }
                } else {
                    if (cb.getSelectedIndex() == 0) {
                        ModelSelects.tblLoadConsumptionAll(TURKEY[HEN], tbl, dtm);
                    } else {
                        ModelSelects.tblLoadConsumptionFunction(cb.getSelectedIndex(),
                               TURKEY[HEN], tbl, dtm);
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