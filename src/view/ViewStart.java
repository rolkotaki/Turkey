
package view;

import com.toedter.calendar.JDateChooser;
import interfaces.RolandTFInterface;
import interfaces.ModelInterface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.swing.*;
import model.ModelSeason;
import model.ModelSeasonDatas;

/**
 * A szezon kezdetekor meg kell adni bizonyos adatokat. Az alábbi felület arra
 * szolgál, hogy ezeket az adatokat elkérje a felhasználótól.
 */
public class ViewStart extends JDialog implements ActionListener, WindowListener {

    private JPanel pnFirst = new JPanel(new BorderLayout());
    private JPanel pnConsumption = new JPanel(new BorderLayout());
    private JPanel pnDatas = new JPanel(new BorderLayout());
    private JPanel pnLast = new JPanel(new BorderLayout());
    private JPanel pnConsCenter = new JPanel();
    private JButton btNextFirst = new JButton("Tovább");
    private JButton btCancelCons = new JButton("Mégse");
    private JButton btCancelDatas = new JButton("Mégse");
    private JButton btNextCons = new JButton("Tovább");
    private JButton btNextDatas = new JButton("Tovább");
    private JButton btFinish = new JButton("Befejez");
    private JDateChooser dc = new JDateChooser(new Date(), ModelInterface.DATEFORMAT); 
    private RolandTextField rtfUnit = new RolandTextField(8, 0, 80, RolandTFInterface.TYPEDOUBLE);
    private RolandTextField rtfStockBuck = new RolandTextField(8, 0, 7000, RolandTFInterface.TYPEINT);
    private RolandTextField rtfStockHen = new RolandTextField(8, 0, 7000, RolandTFInterface.TYPEINT);
    private RolandTextField rtfDay = new RolandTextField(8, 42, 49, RolandTFInterface.TYPEINT);
    private RolandTextField rtfBuckNum = new RolandTextField(8, 0, 5000, RolandTFInterface.TYPEINT);
    private RolandTextField rtfHenNum = new RolandTextField(8, 0, 5000, RolandTFInterface.TYPEINT);
    private RolandTextField rtfBuckWeight = new RolandTextField(8, 0, 5, RolandTFInterface.TYPEDOUBLE);
    private RolandTextField rtfHenWeight = new RolandTextField(8, 0, 5, RolandTFInterface.TYPEDOUBLE);
    private RolandTextField rtfOnStock = new RolandTextField(8, 0, 50000, RolandTFInterface.TYPEINT);
    private ArrayList<RolandTextField> rtfBuckList = new ArrayList<RolandTextField>();
    private ArrayList<RolandTextField> rtfHenList = new ArrayList<RolandTextField>();
    private ArrayList<Double> buckConsList = new ArrayList<Double>();
    private ArrayList<Double> henConsList = new ArrayList<Double>();
    private int x = 0;
    private int y = 0;

    public ViewStart() {
        setTitle("Kezdeti adatok megadása");
        setSize(500, 530);
        setModal(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = (int) ((screen.getWidth() - this.getWidth()) / 2);
        y = (int) ((screen.getHeight() - this.getHeight()) / 2 - 40);
        setLocation(x, y);
        setModal(true);
        
        /*
         * JPanel for information.
         */

        JPanel pnFirstCenter = new JPanel(new GridBagLayout());
        JPanel pnFirstSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        JLabel lbFirst = new JLabel("<html>A szezon kezdete előtt kérem adjon meg néhány<br>fontos adatot!</html>");
        lbFirst.setFont(new Font("Arial", Font.BOLD, 18));
        pnFirstCenter.add(lbFirst);
        pnFirstSouth.add(btNextFirst);
        btNextFirst.addActionListener(this);

        pnFirst.add(pnFirstSouth, BorderLayout.SOUTH);
        pnFirst.add(pnFirstCenter, BorderLayout.CENTER);

        /*
         * End of JPanel for information.
         */

        /*
         * JPanel for basic datas.
         */

        JPanel pnDatasNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JPanel pnDatasSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        JPanel pnDatasCenter = new JPanel();
        pnDatasCenter.setLayout(null);

        JLabel lbDatas = new JLabel("A pulykák érkezésével kapcsolatos adatok");
        lbDatas.setFont(new Font("Arial", Font.BOLD, 18));
        pnDatasNorth.add(lbDatas);

        JLabel lbDate = new JLabel("Érkezés dátuma:");
        lbDate.setBounds(30, 20, 270, 20);
        JLabel lbOnStock = new JLabel("Raktáron (1-50000 kg):");
        lbOnStock.setBounds(30, 60, 270, 20);
        JLabel lbStockBuck = new JLabel("Kezdő takarmánymennyiség (bak) (1-7000 kg):");
        lbStockBuck.setBounds(30, 100, 270, 20);
        JLabel lbStockHen = new JLabel("Kezdő takarmánymennyiség (tojó) (1-7000 kb):");
        lbStockHen.setBounds(30, 140, 270, 20);
        JLabel lbDay = new JLabel("Puykák életkora (43-49 nap):");
        lbDay.setBounds(30, 180, 270, 20);
        JLabel lbBuckNum = new JLabel("Bakok kezdő létszáma (1-5000 db):");
        lbBuckNum.setBounds(30, 220, 270, 20);
        JLabel lbHenNum = new JLabel("Tojók kezdő létszáma (1-5000 db):");
        lbHenNum.setBounds(30, 260, 270, 20);
        JLabel lbUnit = new JLabel("Egy zsák termény (1-80 kg):");
        lbUnit.setBounds(30, 300, 270, 20);
        JLabel lbBuckWeight = new JLabel("Bak kezdeti tömege (0-5 kg):");
        lbBuckWeight.setBounds(30, 340, 270, 20);
        JLabel lbHenWeight = new JLabel("Tojó kezdeti tömege (0-5 kg):");
        lbHenWeight.setBounds(30, 380, 270, 20);
        dc.setBounds(315, 20, 95, 20);
        rtfOnStock.setBounds(330, 60, 80, 20);
        rtfStockBuck.setBounds(330, 100, 80, 20);
        rtfStockHen.setBounds(330, 140, 80, 20);
        rtfDay.setBounds(330, 180, 80, 20);
        rtfBuckNum.setBounds(330, 220, 80, 20);
        rtfHenNum.setBounds(330, 260, 80, 20);
        rtfUnit.setBounds(330, 300, 80, 20);
        rtfBuckWeight.setBounds(330, 340, 80, 20);
        rtfHenWeight.setBounds(330, 380, 80, 20);
        pnDatasCenter.add(lbDate);
        pnDatasCenter.add(lbOnStock);
        pnDatasCenter.add(lbStockBuck);
        pnDatasCenter.add(lbStockHen);
        pnDatasCenter.add(lbDay);
        pnDatasCenter.add(lbBuckNum);
        pnDatasCenter.add(lbHenNum);
        pnDatasCenter.add(lbUnit);
        pnDatasCenter.add(lbBuckWeight);
        pnDatasCenter.add(lbHenWeight);
        pnDatasCenter.add(dc);
        pnDatasCenter.add(rtfOnStock);
        pnDatasCenter.add(rtfStockBuck);
        pnDatasCenter.add(rtfStockHen);
        pnDatasCenter.add(rtfDay);
        pnDatasCenter.add(rtfBuckNum);
        pnDatasCenter.add(rtfHenNum);
        pnDatasCenter.add(rtfUnit);
        pnDatasCenter.add(rtfBuckWeight);
        pnDatasCenter.add(rtfHenWeight);

        pnDatasSouth.add(btNextDatas);
        pnDatasSouth.add(btCancelDatas);
        btCancelDatas.addActionListener(this);
        btNextDatas.addActionListener(this);

        pnDatas.add(pnDatasNorth, BorderLayout.NORTH);
        pnDatas.add(pnDatasSouth, BorderLayout.SOUTH);
        pnDatas.add(pnDatasCenter, BorderLayout.CENTER);

        /*
         * End of JPanel for basic datas.
         */

        /*
         * JPanel for the consumption of turkeys.
         */

        JPanel pnConsNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JPanel pnConsSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pnConsCenter.setLayout(null);

        JLabel lbConsumption = new JLabel("A pulykák számára előírt fogyasztási mennyiségek");
        lbConsumption.setFont(new Font("Arial", Font.BOLD, 18));
        pnConsNorth.add(lbConsumption);

        JLabel lbBuck = new JLabel("Bakok");
        lbBuck.setFont(new Font("Arial", Font.BOLD, 14));
        lbBuck.setBounds(100, 5, 50, 25);
        JLabel lbHen = new JLabel("Tojók");
        lbHen.setFont(new Font("Arial", Font.BOLD, 14));
        lbHen.setBounds(350, 5, 50, 25);
        pnConsCenter.add(lbBuck);
        pnConsCenter.add(lbHen);

        for (int i = 0; i < 17; i++) {
            JLabel lb = new JLabel((i + 7) + ". hét:");
            lb.setBounds(30, i * 30 + 40, 50, 20);
            pnConsCenter.add(lb);
            RolandTextField rtf = new RolandTextField(8, 0, 50, RolandTFInterface.TYPEDOUBLE);
            rtfBuckList.add(rtf);
            rtf.setBounds(100, i * 30 + 40, 80, 20);
            pnConsCenter.add(rtf);
            JLabel lbKg = new JLabel("kg");
            lbKg.setBounds(185, i * 30 + 40, 50, 20);
            pnConsCenter.add(lbKg);
        }
        for (int i = 0; i < 17; i++) {
            JLabel lb = new JLabel((i + 7) + ". hét:");
            lb.setBounds(280, i * 30 + 40, 50, 20);
            pnConsCenter.add(lb);
            RolandTextField rtf = new RolandTextField(8, 0, 50, RolandTFInterface.TYPEDOUBLE);
            rtfHenList.add(rtf);
            rtf.setBounds(350, i * 30 + 40, 80, 20);
            pnConsCenter.add(rtf);
            JLabel lbKg = new JLabel("kg");
            lbKg.setBounds(435, i * 30 + 40, 50, 20);
            pnConsCenter.add(lbKg);
        }

        pnConsSouth.add(btNextCons);
        pnConsSouth.add(btCancelCons);
        btCancelCons.addActionListener(this);
        btNextCons.addActionListener(this);

        pnConsumption.add(pnConsNorth, BorderLayout.NORTH);
        pnConsumption.add(pnConsSouth, BorderLayout.SOUTH);
        pnConsumption.add(pnConsCenter, BorderLayout.CENTER);

        /*
         * End of JPanel for consumption of turkeys.
         */

        /*
         * JPanel for finish.
         */

        JPanel pnLastCenter = new JPanel(new GridBagLayout());
        JPanel pnLastSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        JLabel lbLast = new JLabel("<html>Az adatok mentéséhez kattintson a<br>Befejezés gombra.</html>");
        lbLast.setFont(new Font("Arial", Font.BOLD, 18));
        lbLast.setHorizontalAlignment(JLabel.CENTER);
        lbLast.setVerticalAlignment(JLabel.CENTER);
        lbLast.setHorizontalTextPosition(JLabel.LEADING);
        pnLastCenter.add(lbLast);
        pnLastSouth.add(btFinish);
        btFinish.addActionListener(this);

        pnLast.add(pnLastSouth, BorderLayout.SOUTH);
        pnLast.add(pnLastCenter, BorderLayout.CENTER);

        /*
         * End of JPanel for finish.
         */

        add(pnFirst);
        addWindowListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btNextFirst) {
            remove(pnFirst);
            add(pnDatas);
            this.revalidate();
            rtfOnStock.requestFocus();
        } else if (e.getSource() == btNextDatas) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dc.getDate());
            cal.add(Calendar.DATE, 30);
            int stock = 0;
            int error = 0;
            String message="";
            if (!rtfUnit.getInputVerifier().shouldYieldFocus(rtfUnit) ||
                    rtfUnit.getText().equals("")) {
                error++;
                message+="A zsák váltószáma nem megfelelő!<br>";
            }
            if (!rtfStockBuck.getInputVerifier().shouldYieldFocus(rtfStockBuck) ||
                    rtfStockBuck.getText().equals("")) {
                error++;
                message+="A bakok kezdő takarmánymennyisége nem megfelelő!<br>";
            } else {
                stock+=Integer.parseInt(rtfStockBuck.getText());
            }
            if (!rtfStockHen.getInputVerifier().shouldYieldFocus(rtfStockHen) ||
                    rtfStockHen.getText().equals("")) {
                error++;
                message+="A tojók kezdő takarmánymennyisége nem megfelelő!<br>";
            } else {
                stock+=Integer.parseInt(rtfStockHen.getText());
            }
            if (!rtfOnStock.getInputVerifier().shouldYieldFocus(rtfOnStock) ||
                    rtfOnStock.getText().equals("")) {
                error++;
                message+="A raktárkészlet adat nem megfelelő!<br>";
            } else {
                if (stock>Integer.parseInt(rtfOnStock.getText())) {
                    error++;
                    message+="A kezdő takarmánymennyiségek biztosan nem többek,<br>"
                            + "mint a kekzdő raktárkészlet!<br>";
                }
            }
            if (!rtfDay.getInputVerifier().shouldYieldFocus(rtfDay) ||
                    rtfDay.getText().equals("")) {
                error++;
                message+="A pulykák életkora nem megfelelő!<br>";
            }
            if (!rtfBuckNum.getInputVerifier().shouldYieldFocus(rtfBuckNum) ||
                    rtfBuckNum.getText().equals("")) {
                error++;
                message+="A bakok létszáma nem megfelelő!<br>";
            }
            if (!rtfHenNum.getInputVerifier().shouldYieldFocus(rtfHenNum) ||
                    rtfHenNum.getText().equals("")) {
                error++;
                message+="A tojók létszáma nem megfelelő!<br>";
            }
            if (!rtfBuckWeight.getInputVerifier().shouldYieldFocus(rtfBuckWeight) ||
                    rtfBuckWeight.getText().equals("")) {
                error++;
                message+="A bakok tömege nem megfelelő!<br>";
            }
            if (!rtfHenWeight.getInputVerifier().shouldYieldFocus(rtfHenWeight) ||
                    rtfHenWeight.getText().equals("")) {
                error++;
                message+="A tojók tömege nem megfelelő!<br>";
            }
            if ((dc.getDate().compareTo(new Date())>0) || (cal.getTime().compareTo(new Date())<0)) {
                error++;
                message+="A dátum nem megfelelő! Nem lehet a jövőben, és nem lehet<br>30 napnál régebbi!";
            }
            if (error==0) {
                setSize(500, 670);
                setLocation(x, y - 70);
                remove(pnDatas);
                add(pnConsumption);
                rtfBuckList.get(0).requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>"+message+"</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btNextCons) {
            //a később rendezni kívánt listák
            ArrayList<Double> buckListSorted = new ArrayList<Double>();
            ArrayList<Double> henListSorted = new ArrayList<Double>();
            //a fogyasztási mennyiségeket tartalmazó két listát ürítem, hiszen
            //hogy üres legyen mielőtt újra feltöltöm
            buckConsList.clear();
            henConsList.clear();
            int i=0;
            //ha a számláló kisebb, mint a szövegmezőket tartalmazó lista mérete,
            //ha a szövegmező értéke valid és nem üres
            while (i<rtfBuckList.size() && (rtfBuckList.get(i).getInputVerifier().shouldYieldFocus(rtfBuckList.get(i)) &&
                    !rtfBuckList.get(i).getText().equals(""))) {
                //a RolandTextField szövegmező szövegét double értékké konvertálom
                //és hozzáadom mindkét listához
                buckConsList.add(Double.parseDouble(rtfBuckList.get(i).getText()));
                buckListSorted.add(Double.parseDouble(rtfBuckList.get(i).getText()));
                i++;
            }
            //ha a számmláló megegyezik a listamérettel, akkor minden adat helyes
            boolean okBuck=(i==rtfBuckList.size());
            i=0;
            while(i<rtfHenList.size() && (rtfHenList.get(i).getInputVerifier().shouldYieldFocus(rtfHenList.get(i)) &&
                    !rtfHenList.get(i).getText().equals(""))) {
                henConsList.add(Double.parseDouble(rtfHenList.get(i).getText()));
                henListSorted.add(Double.parseDouble(rtfHenList.get(i).getText()));
                i++;
            }
            boolean okHen=(i==rtfHenList.size());
            //rendezem a két listát
            Collections.sort(buckListSorted);
            Collections.sort(henListSorted);
            //ha minden adat helyes volt
            if (okBuck && okHen) {
                //ha az adatok nem csökkenek egymást követően, akkor továbblép a program,
                if (buckListSorted.equals(buckConsList) && henListSorted.equals(henConsList)) {
                    setSize(500, 360);
                    setLocation(x, y);
                    remove(pnConsumption);
                    add(pnLast);
                } else {
                    JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Az adatok nem csökkenhetnek egymást követően!"+
                        "</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Nincs minden kitöltve, vagy nem helyesek az adatok!<br>"+
                        "Az adatok 0 és 50 kg között lehetnek!</b></font></html>",
                        "Hibás adat!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == btFinish) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (ModelSeason.createSeason(new ModelSeasonDatas(ModelInterface.SDFDATE.format(dc.getDate()), Integer.parseInt(rtfDay.getText()),
                    Integer.parseInt(rtfBuckNum.getText()), Integer.parseInt(rtfHenNum.getText()),
                    Integer.parseInt(rtfStockBuck.getText()), Integer.parseInt(rtfStockHen.getText()),
                    Integer.parseInt(rtfOnStock.getText()), Double.parseDouble(rtfUnit.getText()), 
                    Double.parseDouble(rtfBuckWeight.getText()), Double.parseDouble(rtfHenWeight.getText()),
                    buckConsList, henConsList))) {
                setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(this, "<html><font color=\"green\"><b>Az adatok sikeresen el lettek mentve!</b></font></html>",
                        "Sikeres mentés", JOptionPane.INFORMATION_MESSAGE);
            } else {
                setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(this, "<html><font color=\"red\"><b>Az adatok mentése nem sikerült!</b></font></html>",
                        "Hibás mentés!", JOptionPane.WARNING_MESSAGE);
            }
            dispose();
        } else if (e.getSource() == btCancelDatas || e.getSource() == btCancelCons) {
            dispose();
        }
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
 * Forrás: Alkalmazásfejlesztés technológia tantárgy gyakorlati példái
 * http://www.toedter.com/en/jcalendar/
 * http://www.mkyong.com/java/how-to-compare-dates-in-java/
 */