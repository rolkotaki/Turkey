
package view;

import database.Database;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import model.ModelLogin;

/**
 * A felhasználónév és jelszó megváltoztatásához szükséges dialógusablak.
 */
public class ViewChangeLoginDatas extends JDialog implements ActionListener, WindowListener {
    
    private JButton bt = new JButton("OK");
    private JTextField tfUser = new JTextField();
    private JPasswordField pfOldPassword = new JPasswordField();
    private JPasswordField pfPassword = new JPasswordField();
    private JPasswordField pfPwdAgain = new JPasswordField();
    private JLabel lb = new JLabel();
    
    public ViewChangeLoginDatas() {
        setTitle("Bejelentkezési adatok módosítása");
        setSize(450, 335);
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

        JLabel lbTitle = new JLabel("Bejelentkezési adatok megváltoztatása");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pnNorth.add(lbTitle);
        
        JLabel lbUserName = new JLabel("Új felhasználónév (1-25 karakter):");
        lbUserName.setBounds(35, 15, 200, 20);
        JLabel lbOldPwd = new JLabel("Régi jelszó:");
        lbOldPwd.setBounds(35, 50, 180, 20);
        JLabel lbPassword = new JLabel("Új jelszó:");
        lbPassword.setBounds(35, 85, 180, 20);
        JLabel lbPwdAgain = new JLabel("Új jelszó újra:");
        lbPwdAgain.setBounds(35, 120, 180, 20);
        pnCenter.add(lbUserName);
        pnCenter.add(lbOldPwd);
        pnCenter.add(lbPassword);
        pnCenter.add(lbPwdAgain);
        tfUser.setBounds(250, 15, 140, 23);
        pfOldPassword.setBounds(250, 50, 140, 23);
        pfPassword.setBounds(250, 85, 140, 23);
        pfPwdAgain.setBounds(250, 120, 140, 23);
        tfUser.setFont(new Font("Arial", Font.PLAIN, 14));
        pnCenter.add(tfUser);
        pnCenter.add(pfOldPassword);
        pnCenter.add(pfPassword);
        pnCenter.add(pfPwdAgain);
        lb.setBounds(55, 155, 250, 65);
        pnCenter.add(lb);
        
        pnSouth.add(bt);
        bt.addActionListener(this);
                
        add(pnNorth, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);
        
        addWindowListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        lb.setText("");
        String message = "";
        boolean success = true;
        if (!ModelLogin.checkNewUserName(tfUser.getText())) {
            message += "Helytelen felhasználónév! 1-25 karakter!<br>";
            success = false;
        }
        if (!ModelLogin.checkPassword(new String(pfOldPassword.getPassword()))) {
            message += "Helytelen jelszó!<br>";
            success = false;
        }
        if (!ModelLogin.checkNewPassword(new String(pfPassword.getPassword()),
                new String(pfPwdAgain.getPassword()))) {
            message += "Az új jelszók nem egyeznek!";
            success = false;
        }
        if (success) {
            if (Database.updateLoginDatas(tfUser.getText(), new String(pfPassword.getPassword())))
                JOptionPane.showMessageDialog(this, "<html><font color=\"green\"><b>Az adatok sikeresen módosultak!</b></font></html>",
                        "Siker!", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            lb.setText("<html><font color=\"red\"><b>" + message + "</b></font></html>");
        }
    }
    
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
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
