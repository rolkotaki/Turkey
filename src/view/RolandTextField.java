
package view;

import interfaces.RolandTFInterface;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;


/**
 * A RolandTextField egy saját komponens, amelyet a JFormattedTextField
 * osztályból örökítettem. A komponens ellenőrzi a leütött billentyűket, ha nem
 * megfelelő billentyűt ütünk le, akkor piros színű lesz a háttér, sípol, és a
 * leütött karakter érvénytelen lesz, tehát nem jelenik meg a szövegmezőben. A
 * leüthető karakterek: számok, egy darab pont (Double érték esetén), Enter,
 * Delete, Backspace és a jobbra-balra nyilak. Megadhatunk továbbá minimum és
 * maximum értéket, ebben az esetben a komponens csak a két érték közötti
 * számokat fogadja el. Az is kiválasztható, hogy Integer vagy Double értékeket
 * fogadjon el.
 */
public class RolandTextField extends JFormattedTextField implements RolandTFInterface, KeyListener {

    private int columns;
    private int minimum, maximum;
    private int type = -1;

    public RolandTextField(int type) {
        this.type = type;
        addKeyListener(this);
    }

    public RolandTextField(int columns, int type) {
        this.columns = columns;
        this.type = type;
        setColumns(columns);
        addKeyListener(this);
    }

    public RolandTextField(int min, int max, int type) {
        this.minimum = min;
        this.maximum = max;
        this.type = type;
        addKeyListener(this);
    }

    public RolandTextField(int columns, int min, int max, int type) {
        this.columns = columns;
        this.minimum = min;
        this.maximum = max;
        this.type = type;
        setColumns(columns);
        /*
         * Az InputVerifier felüldefiniálása annak érdekében, hogy csak a
         * minimum és maximum érték között számokat fogadja el.
         */
        setInputVerifier(new InputVerifier() {

            RolandTextField ftf;
            double numValue;
            int min = RolandTextField.this.minimum;
            int max = RolandTextField.this.maximum;

            @Override
            public boolean verify(JComponent input) {
                boolean valid = true;
                if (!getText().equals("")) {
                    valid=false;
                    ftf = (RolandTextField) input;
                    String text = ftf.getText();
                    try {
                        numValue = Double.parseDouble(text);
                        valid = true;
                    } catch (Exception pe) {
                        valid = false;
                    }
                    if (valid) {
                        if ((numValue <= min) || (numValue > max)) {
                            valid = false;
                        }
                    }
                }
                return valid;
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
                boolean valid = verify(input);
                if (!valid) {
                    Toolkit.getDefaultToolkit().beep();
                }
                return valid;
            }
        });

        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Figyeljük a leütött billenytyűket. Ha nem megfelelő billentyűt ütünk le,
     * akkor a komponens háttere piros lesz, és sípolni fog a számítógép. Ha
     * Double értékről van szó, akkor egy pont leüthető, de annál többet már nem
     * enged.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (type == 1) {
            if (this.getText().indexOf(".") != -1) {
                if (e.getKeyChar() == KeyEvent.VK_PERIOD) {
                    Toolkit.getDefaultToolkit().beep();
                    ((JTextField) e.getSource()).setEditable(false);
                    ((JTextField) e.getSource()).setBackground(Color.red);
                } else {
                    if ((VALIDDOUBLECHARACTERS.indexOf(e.getKeyChar()) == -1)
                            && (e.getKeyChar() != KeyEvent.VK_DELETE)
                            && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) && (e.getKeyChar() != KeyEvent.VK_ENTER)
                            && (e.getKeyCode() != KeyEvent.VK_LEFT) && (e.getKeyCode() != KeyEvent.VK_RIGHT)) {
                        Toolkit.getDefaultToolkit().beep();
                        ((JTextField) e.getSource()).setEditable(false);
                        ((JTextField) e.getSource()).setBackground(Color.red);
                    }
                }
            } else {
                if ((VALIDDOUBLECHARACTERS.indexOf(e.getKeyChar()) == -1)
                        && (e.getKeyChar() != KeyEvent.VK_DELETE)
                        && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) && (e.getKeyChar() != KeyEvent.VK_ENTER)
                        && (e.getKeyCode() != KeyEvent.VK_LEFT) && (e.getKeyCode() != KeyEvent.VK_RIGHT)) {
                    Toolkit.getDefaultToolkit().beep();
                    ((JTextField) e.getSource()).setEditable(false);
                    ((JTextField) e.getSource()).setBackground(Color.red);
                }
            }
        } else {
            if ((VALIDINTCHARACTERS.indexOf(e.getKeyChar()) == -1)
                    && (e.getKeyChar() != KeyEvent.VK_DELETE)
                    && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) && (e.getKeyChar() != KeyEvent.VK_ENTER)
                    && (e.getKeyCode() != KeyEvent.VK_LEFT) && (e.getKeyCode() != KeyEvent.VK_RIGHT)) {
                Toolkit.getDefaultToolkit().beep();
                ((JTextField) e.getSource()).setEditable(false);
                ((JTextField) e.getSource()).setBackground(Color.red);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ((JTextField) e.getSource()).setEditable(true);
        ((JTextField) e.getSource()).setBackground(Color.white);
    }
}

/*
 * Forrás: Kaczur Sándor Programozási technológia című könyvének 18. fejezete
 * (MásodfokúEgyenlet4.java mintapélda).
 * http://prog.hu/tudastar/161941/JFormattedTextField.html
 */