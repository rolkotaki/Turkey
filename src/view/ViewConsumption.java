
package view;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.ConsumptionPrescribed;
import model.ModelArriveDatas;

/**
 * JDialog, amely a pulykák számára előírt fogyasztási mennyiségeket
 * jeleníti meg.
 */

public class ViewConsumption extends JDialog implements WindowListener {
    
    public ViewConsumption () {
        ModelArriveDatas.setCons(new ConsumptionPrescribed());
        setTitle("Előírt fogyasztási mennyiségek");
        setSize(500, 640);
        setResizable(false);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        JPanel pnCenter = new JPanel();
        pnCenter.setLayout(null);
        
        JLabel lbTitle = new JLabel("A pulykák számára előírt fogyasztási mennyiségek");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        JLabel lbBuck = new JLabel("Bakok");
        lbBuck.setFont(new Font("Arial", Font.BOLD, 14));
        lbBuck.setBounds(100, 5, 50, 25);
        JLabel lbHen = new JLabel("Tojók");
        lbHen.setFont(new Font("Arial", Font.BOLD, 14));
        lbHen.setBounds(350, 5, 50, 25);
        pnCenter.add(lbBuck);
        pnCenter.add(lbHen);
        
        for (int i=0; i<17; i++) {
            JLabel lb = new JLabel((i+7)+". hét:");
            lb.setBounds(30, i*30+40, 50, 20);
            pnCenter.add(lb);
            JLabel lbAmount = new JLabel();
            lbAmount.setText(ModelArriveDatas.getCons().getBuckCons().get(i).toString());
            lbAmount.setBounds(105, i*30+40, 80, 20);
            pnCenter.add(lbAmount);
            JLabel lbKg = new JLabel("kg");
            lbKg.setBounds(160, i*30+40, 50, 20);
            pnCenter.add(lbKg);
        }
        for (int i=0; i<17; i++) {
            JLabel lb = new JLabel((i+7)+". hét:");
            lb.setBounds(280, i*30+40, 50, 20);
            pnCenter.add(lb);
            JLabel lbAmount = new JLabel();
            lbAmount.setText(ModelArriveDatas.getCons().getHenCons().get(i).toString());
            lbAmount.setBounds(355, i*30+40, 80, 20);
            pnCenter.add(lbAmount);
            JLabel lbKg = new JLabel("kg");
            lbKg.setBounds(410, i*30+40, 50, 20);
            pnCenter.add(lbKg);
        }
        
        add(pnNorth, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        
        addWindowListener(this);
        setVisible(true);
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
