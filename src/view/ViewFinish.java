
package view;

import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import model.ModelSeason;

/*
 * Ha véget ér egy szezon, akkor a felhasználónak ezt a szoftver felé is
 * jeleznie kell. A szezon befejezésekor meg kell adnunk egy helyet, hogy a
 * PDF és Excel fájlok hová mentődjön el.
 */

public class ViewFinish extends JDialog implements ActionListener, ViewInterface {
    
    private JButton btBrowse = new JButton("Tallóz...");
    private JButton btFinish = new JButton("Befejez");
    private JButton btCancel = new JButton("Mégse");
    private JTextField tfPath = new JTextField(25);
    private JFileChooser fc = new JFileChooser("c:/");
    private String filePath=null;
    
    public ViewFinish () {
        setTitle("Szezon befejezése");
        setSize(540, 250);
        setResizable(false);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JPanel pnCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnNorth.setPreferredSize(new Dimension(500, 120));
        
        JLabel lbTitle = new JLabel("A szezon befejezése");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        pnNorth.add(new JLabel("<html>A szezon befejezése során készülni fog egy PDF kimutatás a szezonról.<br>"
                + "Emellett az összes táblázat alapján készülni fog egy Ecxel fájl, amelyben a<br>"
                + "táblázatok munkalapként fognak megjelenni.<br>"
                + "Kérem adja meg, hogy a PDF és Excel fájlokat tartalmazó mappa hova mentődjön el!<br>"
                + "A szezon befejezése eltarthat néhány másodpercig.</html>"));
        
        pnCenter.add(new JLabel("PDF és Excel fájlok helye:"));
        tfPath.setEditable(false);
        pnCenter.add(tfPath);
        pnCenter.add(btBrowse);
        btBrowse.addActionListener(this);
        
        pnSouth.add(btFinish);
        pnSouth.add(btCancel);
        btCancel.addActionListener(this);
        btFinish.addActionListener(this);
        
        add(pnNorth, BorderLayout.NORTH);
        add(pnSouth, BorderLayout.SOUTH);
        add(pnCenter, BorderLayout.CENTER);
        
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==btBrowse) {
            fc.setDialogTitle("Tallózás");
            fc.setDialogType(JFileChooser.OPEN_DIALOG);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
                File fcFile = fc.getSelectedFile();
                tfPath.setText(fcFile.getPath());
                filePath=fcFile.getPath().replace("\\", "/");
            }
        } else if (e.getSource()==btFinish) {
            if(filePath!=null) {
                int option=JOptionPane.showConfirmDialog(this, "Biztos benne, hogy befejezi a szezont?",
                        "Megerősítés", JOptionPane.YES_NO_OPTION);
                if(option==JOptionPane.YES_OPTION) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if(ModelSeason.finishSeason(filePath)) {
                        setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(this, "<html><font color=\"green\"><b>A szezon sikeresen be lett fejezve!</b></font></html>",
                        "Sikeres befejezés!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>A szezon befejezése közben hiba történt!</b></font></html>",
                        "Nem sikerült!", JOptionPane.WARNING_MESSAGE);
                    }
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Nincs kiválasztva mappa!</b></font></html>",
                        "Válasszon egy mappát!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource()==btCancel) {
            this.dispose();
        }
    }
    
}
