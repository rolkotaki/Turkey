package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.ModelLogin;

/**
 * Bejelentkező ablak. A program indulásakor ez jelenik meg, és csak a helyes
 * felhasználónév és jelszó begépelése után tudunk dolgozni a programban.
 */
public class ViewLogin extends JDialog implements ActionListener, WindowListener, KeyListener {

    private JButton btLogin = new JButton("Bejelentkezik");
    private JButton btExit = new JButton("Kilép");
    private JTextField tfUser = new JTextField();
    private JPasswordField pfPassword = new JPasswordField();
    private JLabel lb = new JLabel();

    public ViewLogin() {
        setTitle("Bejelentkezés");
        setSize(350, 240);
        setResizable(false);
        setModal(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screen.getHeight() - this.getHeight()) / 2 - 40);
        setLocation(x, y);

        JPanel pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel pnCenter = new JPanel();
        pnCenter.setLayout(null);

        JLabel lbTitle = new JLabel("Bejelentkezés");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);

        JLabel lbUser = new JLabel("Felhasználónév:");
        JLabel lbPassword = new JLabel("Jelszó:");
        lbUser.setBounds(30, 20, 100, 20);
        lbPassword.setBounds(30, 55, 100, 20);
        tfUser.setBounds(140, 20, 130, 23);
        pfPassword.setBounds(140, 55, 130, 23);
        tfUser.setFont(new Font("Arial", Font.PLAIN, 14));
        pnCenter.setPreferredSize(new Dimension(350, 200));
        pnCenter.add(lbUser);
        pnCenter.add(lbPassword);
        pnCenter.add(tfUser);
        pnCenter.add(pfPassword);
        lb.setBounds(40, 90, 150, 40);
        pnCenter.add(lb);
        tfUser.addKeyListener(this);
        pfPassword.addKeyListener(this);

        pnSouth.add(btLogin);
        pnSouth.add(btExit);
        btExit.addActionListener(this);
        btLogin.addActionListener(this);

        add(pnNorth, BorderLayout.NORTH);
        add(pnSouth, BorderLayout.SOUTH);
        add(pnCenter, BorderLayout.CENTER);

        tfUser.requestFocus();
        addWindowListener(this);
        setVisible(true);
    }

    private void loginCheck() {
        lb.setText("");
        String message = "";
        boolean success = true;
        if (!ModelLogin.checkUserName(tfUser.getText())) {
            message += "Helytelen felhasználónév!<br>";
            success = false;
        }
        if (!ModelLogin.checkPassword(new String(pfPassword.getPassword()))) {
            message += "Helytelen jelszó!";
            success = false;
        }
        if (success) {
            ModelLogin.setCorrectDatas(true);            
            this.dispose();
        } else {
            ModelLogin.setCorrectDatas(false);
            lb.setText("<html><font color=\"red\"><b>" + message + "</b></font></html>");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btLogin) {
            loginCheck();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        ModelLogin.setCorrectDatas(ModelLogin.isCorrectDatas());
        System.exit(0);
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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            loginCheck();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
