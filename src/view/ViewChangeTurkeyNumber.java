
package view;

import com.toedter.calendar.JDateChooser;
import database.Database;
import interfaces.RolandTFInterface;
import interfaces.ModelInterface;
import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import javax.swing.*;

/**
 * Az alábbi dialógusablak segítségével a felhasználó megváltoztathatja a
 * bakok és tojók létszámát. Erre azért van szükség, mert a pulykákat nem
 * mindig jól válogatják szét, ezért a bakokhoz kerülhetnek tojók, és fordítva.
 */
public class ViewChangeTurkeyNumber extends JDialog implements ActionListener, WindowListener {
    
    private JDateChooser dc = new JDateChooser(new Date(), ModelInterface.DATEFORMAT);
    private RolandTextField rtf = new RolandTextField(8, 0, 1000, RolandTFInterface.TYPEINT);
    private JComboBox cb = new JComboBox(ViewInterface.CBTURNUMCHANGE);
    private JButton bt = new JButton("OK");
    
    public ViewChangeTurkeyNumber() {
        setTitle("Átminősítés");
        setSize(410, 240);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JPanel pnCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        JPanel pnCenter1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JPanel pnCenter2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JLabel lbTitle = new JLabel("Átminősítés");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        dc.setPreferredSize(new Dimension(95,20));
        pnCenter1.add(new JLabel("Átminősítés napja:"));
        pnCenter1.add(dc);
        pnCenter1.add(new JLabel("Irány:"));
        pnCenter1.add(cb);
        pnCenter2.add(new JLabel("Átminősített darabszám:"));
        pnCenter2.add(rtf);
        pnCenter.add(pnCenter1, BorderLayout.NORTH);
        pnCenter.add(pnCenter2, BorderLayout.SOUTH);
        
        pnSouth.add(bt);
        bt.addActionListener(this);
        
        add(pnNorth, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);
        
        addWindowListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==bt) {
            if (dc.getDate().before(Database.selectArriveDate()) || dc.getDate().after(new Date()) ) {
                JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>A dátum biztosan nagyobb az érkezési "
                        + "dátumnál,<br>illetve kissebb a mai dátumnál!</b></font></html>",
                            "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            } else {
                if (rtf.getInputVerifier().shouldYieldFocus(rtf) && !rtf.getText().equals("")) {
                    if(Database.insertTurkeyNumberChange(ModelInterface.SDFDATE.format(dc.getDate()),
                            rtf.getText(), cb.getSelectedIndex()))
                        JOptionPane.showMessageDialog(this, "<html><font color=\"green\"><b>"
                                + "Az adatok sikeresen el lettek mentve!</b></font></html>",
                            "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Az érték "
                            + "legalább 1 és maximum 1000!</b></font></html>",
                            "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                }
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
