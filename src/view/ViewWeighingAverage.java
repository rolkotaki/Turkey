
package view;

import interfaces.RolandTFInterface;
import interfaces.ViewInterface;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.ModelAverageWeight;
import model.ModelTurkeyWeight;
import model.TurkeyWeight;

/**
 * Amennyiben a főképernyő Heti mérések fülön új mérési adatot szeretnénk
 * bevinni, akkor ez az ablak jelenik meg. Bevihetjük az összes mérési adatot,
 * és a program gombnyomásra kiszámolja a mérési átlagot. Majd ezt az átlagot
 * utána bevihetjük a megfelelő táblázatba. Ehhez két ComboBox-ból ki kell
 * választanunk, hogy melyik hétről és melylik pulykanemről van szó.
 */
public class ViewWeighingAverage extends JDialog implements ActionListener, KeyListener, WindowListener {

    private JButton btNew = new JButton("Bevisz");
    private JButton btAverage = new JButton("Átlagot számol");
    private JButton btDelete = new JButton("Töröl");
    private JButton btAdd = new JButton("Mérést hozzáad");
    private JLabel lb = new JLabel();
    private RolandTextField rtf = new RolandTextField(5, 0, 100, RolandTFInterface.TYPEDOUBLE);
    private DefaultListModel dlm = new DefaultListModel();
    private JList list = new JList(dlm);
    private JScrollPane spList = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JComboBox cbWeek = new JComboBox(ViewInterface.WEEK);
    private JComboBox cbBuckHen = new JComboBox(ViewInterface.BUCKHEN);
    private double averageWeigh = 0;

    public ViewWeighingAverage() {
        setTitle("Mérési átlag számítása");
        setSize(300, 350);
        setResizable(false);
        setModal(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screen.getHeight() - this.getHeight()) / 2 - 40);
        setLocation(x, y);
        setLayout(new BorderLayout());
        
        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 13));
        pnNorth.setPreferredSize(new Dimension(300, 105));
        cbWeek.setPreferredSize(new Dimension(50, 27));
        cbBuckHen.setPreferredSize(new Dimension(80, 27));        
        pnNorth.add(cbWeek);
        pnNorth.add(new JLabel(". hét   "));
        pnNorth.add(cbBuckHen);
        btAdd.setEnabled(false);
        pnNorth.add(btAdd);
        btAdd.addActionListener(this);

        JPanel pnWest = new JPanel();
        pnWest.setPreferredSize(new Dimension(180, 200));
        pnWest.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        btNew.addActionListener(this);
        btAverage.addActionListener(this);
        btDelete.addActionListener(this);
        btAverage.setEnabled(false);
        btDelete.setEnabled(false);
        rtf.addKeyListener(this);
        lb.setFont(new Font("Arial", Font.BOLD, 16));
        pnWest.add(btNew);
        pnWest.add(rtf);
        pnWest.add(btDelete);
        pnWest.add(btAverage);
        pnWest.add(lb);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setMaximumSize(new Dimension(50, 230));
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public int getHorizontalAlignment() {
                return CENTER;
            }
        });
        spList.setPreferredSize(new Dimension(100, 230));

        pnWest.setBorder(BorderFactory.createTitledBorder("Átlag számítása"));
        pnNorth.setBorder(BorderFactory.createTitledBorder("Mérési adat bevitele"));

        add(pnWest, BorderLayout.WEST);
        add(pnNorth, BorderLayout.NORTH);
        add(spList, BorderLayout.CENTER);

        addWindowListener(this);
        setModal(true);
        setVisible(true);
    }

    private void addToList() {
        if (rtf.getInputVerifier().shouldYieldFocus(rtf) && !rtf.getText().equals("")) {
                dlm.addElement("" + rtf.getText());
                btDelete.setEnabled(true);
                btAverage.setEnabled(true);
        }
        rtf.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btNew) {
            addToList();
        } else if (e.getSource() == btDelete) {
            if (list.getSelectedIndex() != (-1)) {
                dlm.removeElementAt(list.getSelectedIndex());
                if (list.getModel().getSize() == 0) {
                    btDelete.setEnabled(false);
                    btAverage.setEnabled(false);
                }
            }
        } else if (e.getSource() == btAverage) {
            ModelAverageWeight maw = new ModelAverageWeight();
            averageWeigh=maw.countAverage(dlm);
            lb.setText("Átlag: " + averageWeigh);
            dlm.removeAllElements();
            btDelete.setEnabled(false);
            btAverage.setEnabled(false);
            btAdd.setEnabled(true);
        } else if (e.getSource() == btAdd) {
            if (averageWeigh > 0) {
                ModelTurkeyWeight.setTw(new TurkeyWeight((cbWeek.getSelectedIndex()+1),
                        cbBuckHen.getSelectedItem().toString(), averageWeigh));
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "A tömeg nem lehet nulla!",
                        "Hibás tömeg!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            addToList();
            rtf.requestFocus();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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

/*
 * Forrás: http://forums.whirlpool.net.au/archive/1686652
 */
