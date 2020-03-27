
package view;

import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.ModelIndexNumber;

/**
 * Egy JDialog, amely megjelenítni mind a heti, mind az összesített fajlagos
 * mutatószámokat. A Számol gombra kattintva a két táblázatban megjelennek a
 * mutatószámok.
 */

public class ViewIndexNumbers extends JDialog implements ViewInterface, ActionListener, WindowListener {
    
    private JButton btCount = new JButton("Számol");
    private JButton btCancel = new JButton("Mégse");
    private DefaultTableModel dtmWeek = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
        @Override
        public Class getColumnClass(int col) {
            if (col==0)
                return Integer.class;
            else
                return Double.class;
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
    private DefaultTableModel dtmCum = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
        @Override
        public Class getColumnClass(int col) {
            return Integer.class;
        }
    };
    
    public ViewIndexNumbers () {
        setTitle("Fajlagos mutatószámok");
        setSize(750, 500);
        setResizable(false);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel pnCenter = new JPanel(new GridLayout(1, 2));
        JPanel pnLeft = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel pnRight = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JLabel lbTitle = new JLabel("Fajlagos mutatószámok");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        JLabel lbWeek = new JLabel("Heti mutatószámok");
        lbWeek.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lbCum = new JLabel("Összesített mutatószámok");
        lbCum.setFont(new Font("Arial", Font.BOLD, 14));
        JTable tblWeek = new JTable();
        dtmWeek.setColumnIdentifiers(ViewInterface.INDEXNUMBER);
        tblWeek.setModel(dtmWeek);
        dtmWeek.setRowCount(0);
        JTable tblCum = new JTable();
        dtmCum.setColumnIdentifiers(ViewInterface.INDEXNUMBER);
        tblCum.setModel(dtmCum);
        dtmCum.setRowCount(0);
        Dimension dim = tblWeek.getPreferredSize();
        JScrollPane spWeek = new JScrollPane(tblWeek);
        spWeek.setPreferredSize(new Dimension(dim.width, tblWeek.getRowHeight()*20));
        JScrollPane spCum = new JScrollPane(tblCum);
        spCum.setPreferredSize(new Dimension(dim.width, tblCum.getRowHeight()*20));
        
        pnLeft.add(lbWeek);
        pnLeft.add(spWeek);
        pnRight.add(lbCum);
        pnRight.add(spCum);
        pnCenter.add(pnLeft);
        pnCenter.add(pnRight);
        
        pnSouth.add(btCount);
        pnSouth.add(btCancel);        
        btCancel.addActionListener(this);
        btCount.addActionListener(this);
        
        add(pnNorth, BorderLayout.NORTH);
        add(pnSouth, BorderLayout.SOUTH);
        add(pnCenter, BorderLayout.CENTER);
        
        addWindowListener(this);
        setVisible(true);
    }

    /**
     * Eseménykezelés.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==btCount) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ModelIndexNumber.tableWeekLoad(dtmWeek, this);
            ModelIndexNumber.tableCumLoad(dtmCum, this);
            setCursor(Cursor.getDefaultCursor());
        } else if (e.getSource()==btCancel) {
            this.dispose();
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
