package vsms;

import javax.swing.*;

/** Main window that hosts the MainPanel. */
public class MainFrame extends JFrame {
    public MainFrame(Repo repo) {
        super("Vehicle Service Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setContentPane(new MainPanel(repo, () -> {
            dispose();
            new LoginFrame(repo).setVisible(true);
        }));
    }
}
