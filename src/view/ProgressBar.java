
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * A táblázatok betöltése a kezdeti időszakban rövid időt vesz igénybe. Azonban
 * két hónap múlva már több másodpercet is igénybe vehet az összes táblázat
 * betöltése, ezért szükséges folyamatindikátor használata. A felhasználónak
 * egy határozatlan idejű folyamatindikátorral jelezni kell, hogy töltődnek a
 * táblázatok.
 */
public class ProgressBar extends JDialog implements ActionListener {
    
    private JProgressBar pb;
    private JButton bt = new JButton("Kilép");
    
    public ProgressBar() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(330,160);
        setTitle("Betöltés");
        setModal(true);
        setLocationRelativeTo(this);
        
        pb = new JProgressBar(0, 100);
        
        JPanel pnCenter = new JPanel(null);
        JPanel pnSouth = new JPanel(new FlowLayout());
        JLabel lb = new JLabel("Adatok betöltése ...");
        lb.setBounds(15, 15, 250, 20);
        pb.setBounds(15, 50, 280, 20);
        
        pnCenter.add(lb);
        pnCenter.add(pb);
        pnSouth.add(bt);
        add(pnCenter, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);
        
        bt.addActionListener(this);
        
        pb.setIndeterminate(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
    
}

/*
 * Forrás: http://www.java2s.com/Code/Java/Swing-JFC/CreatingaJProgressBarComponentwithanUnknownMaximum.htm
 */