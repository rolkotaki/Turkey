
package view;

import interfaces.ModelInterface;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.*;

/**
 * JDialog, amely a szezon kezdetekor megadott adatokat jeleníti meg.
 */

public class ViewArriveDatas extends JDialog implements ModelInterface, WindowListener {
    
    public ViewArriveDatas () {
        ModelArriveDatas.setArriveAge(new ArriveAge());
        ModelArriveDatas.setArriveDate(new ArriveDate());
        ModelArriveDatas.setArriveNumber(new ArriveNumber());
        ModelArriveDatas.setStockStart(new StockStart());
        ModelArriveDatas.setArriveWeight(new ArriveWeight());
        setTitle("Kezdeti adatok");
        setSize(420, 440);
        setResizable(false);
        setModal(true);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        int x=(int)((screen.getWidth()-this.getWidth())/2);
        int y=(int)((screen.getHeight()-this.getHeight())/2-40);
        setLocation(x,y);
        
        JPanel pnNorth = new JPanel();
        JPanel pnCenter = new JPanel();
        pnCenter.setLayout(null);
        
        JLabel lbTitle = new JLabel("A szezon kezdeti adatai");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        JLabel lbDate = new JLabel("Érkezés dátuma:");
        lbDate.setBounds(30, 20, 190, 20);        
        JLabel lbDay = new JLabel("Puykák életkora (nap):");
        lbDay.setBounds(30, 60, 190, 20);
        JLabel lbBuckNum = new JLabel("Bakok kezdő létszáma (db):");
        lbBuckNum.setBounds(30, 100, 190, 20);
        JLabel lbHenNum = new JLabel("Tojók kezdő létszáma (db):");
        lbHenNum.setBounds(30, 140, 190, 20);
        JLabel lbStockBuck = new JLabel("Bak kezdő takarmánymennyiség (kg):");
        lbStockBuck.setBounds(30, 180, 220, 20);
        JLabel lbStockHen = new JLabel("Tojó kezdő takarmánymennyiség (kg):");
        lbStockHen.setBounds(30, 220, 220, 20);
        JLabel lbOnStock = new JLabel("Kezdeti raktárkészlet (kg):");
        lbOnStock.setBounds(30, 260, 220, 20);
        JLabel lbBuckWeight = new JLabel("Bakok kezdeti tömege (kg):");
        lbBuckWeight.setBounds(30, 300, 220, 20);
        JLabel lbHenWeight = new JLabel("Tojók kezdeti tömege (kg):");
        lbHenWeight.setBounds(30, 340, 220, 20);
        pnCenter.add(lbDate);        
        pnCenter.add(lbDay);
        pnCenter.add(lbBuckNum);
        pnCenter.add(lbHenNum);
        pnCenter.add(lbStockBuck);
        pnCenter.add(lbStockHen);
        pnCenter.add(lbOnStock);
        pnCenter.add(lbBuckWeight);
        pnCenter.add(lbHenWeight);
        
        JLabel lbd = new JLabel(ModelArriveDatas.getArriveDate().toString());
        lbd.setBounds(280, 20, 80, 20);
        pnCenter.add(lbd);
        JLabel lbAge = new JLabel(""+ModelArriveDatas.getArriveAge().getAge());
        lbAge.setBounds(280, 60, 80, 20);
        pnCenter.add(lbAge);
        JLabel lbBN = new JLabel(ModelArriveDatas.getArriveNumber().getArriveNumber().get(BUCK).toString());
        lbBN.setBounds(280, 100, 80, 20);
        pnCenter.add(lbBN);
        JLabel lbHN = new JLabel(ModelArriveDatas.getArriveNumber().getArriveNumber().get(HEN).toString());
        lbHN.setBounds(280, 140, 80, 20);
        pnCenter.add(lbHN);
        JLabel lbBS = new JLabel(ModelArriveDatas.getStockStart().getStockStart().get(BUCK).toString());
        lbBS.setBounds(280, 180, 80, 20);
        pnCenter.add(lbBS);
        JLabel lbHS = new JLabel(ModelArriveDatas.getStockStart().getStockStart().get(HEN).toString());
        lbHS.setBounds(280, 220, 80, 20);
        pnCenter.add(lbHS);
        JLabel lbOS = new JLabel(ModelArriveDatas.getStockStart().getStockStart().get(ALL).toString());
        lbOS.setBounds(280, 260, 80, 20);
        pnCenter.add(lbOS);
        JLabel lbBW = new JLabel(ModelArriveDatas.getArriveWeight().getWeightList().get(BUCK_WEIGHT).toString());
        lbBW.setBounds(280, 300, 80, 20);
        pnCenter.add(lbBW);
        JLabel lbHW = new JLabel(ModelArriveDatas.getArriveWeight().getWeightList().get(HEN_WEIGHT).toString());
        lbHW.setBounds(280, 340, 80, 20);
        pnCenter.add(lbHW);
        
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
