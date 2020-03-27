
package view;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.ModelArriveDatas;
import model.UnitFodder;

/**
 * A szezon kezdetekor megadott mértékegységeket jeleníti meg.
 */

public class ViewUnit extends JDialog implements WindowListener {
    
    public ViewUnit () {
        ModelArriveDatas.setUnitFodder(new UnitFodder());
        setTitle("Mértékegység");
        setSize(400, 185);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JPanel pnCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
        
        JLabel lbTitle = new JLabel("Az ön által megadott mértékegységek");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        JLabel lbUnit = new JLabel("1 zsák termény:   "+ModelArriveDatas.getUnitFodder().getUnit());
        pnCenter.add(lbUnit);
        
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
