package vsms;

import javax.swing.*;

/** Login window that hosts the LoginPanel. */
public class LoginFrame extends JFrame {
    public LoginFrame(Repo repo) {
        super("Vehicle Service Management System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        LoginPanel p = new LoginPanel(repo, () -> {
            dispose();
            new MainFrame(repo).setVisible(true);
        });
        setContentPane(p);
        getRootPane().setDefaultButton(p.loginButton);
    }
}
