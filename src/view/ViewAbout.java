
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A szoftver névjegyének felülete.
 */
public class ViewAbout extends JDialog implements WindowListener {
    
    public ViewAbout() {
        setTitle("Névjegy");
        setSize(500, 620);
        setModal(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screen.getHeight() - this.getHeight()) / 2 - 40);
        setLocation(x, y);
        setModal(true);
        
        JPanel pnImage = new JPanel();
        JPanel pnText = new JPanel();
        
        JLabel lbImage = new JLabel(new ImageIcon("./files/turkey.jpg"));
        pnImage.add(lbImage, BorderLayout.CENTER);
        JLabel lbText = new JLabel();
        lbText.setText("<html><b>Program neve, verziója: Pulyka 1.0</b><br><br>"
                + "A program egy pulykák nevelésével kapcsolatos szezon nyilvántartására hivatott.<br>"
                + "Segítségével könnyedén nyilvántarthatóak a pulykák létszámával és fogyasztásával<br>"
                + "kapcsolatos adatok, a raktárkészlettel kapcsolatos adatok, valamint a pulykák<br>"
                + "tömegével kapcsolatos adatok.<br>A szoftver segítségével bármely pulykanevelő könnyedén"
                + " kiszámolhatja a különböző<br>fajlagos mutatószámokat, illetve készíthet "
                + "PDF és Excel kimutatásokat.<br><br>Fejlesztő: Takács Roland<br>Email: roland0208@freemail.hu</html>");
        pnText.add(lbText);
        
        add(pnImage, BorderLayout.NORTH);
        add(pnText, BorderLayout.CENTER);
        
        addWindowListener(this);
        setVisible(true);
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
}
