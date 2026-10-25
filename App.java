package vsms;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

/** Entry point.  Run with --demo to use built-in sample data instead of MySQL. */
public class App {
    public static void main(String[] args) {
        Ui.install();
        SwingUtilities.invokeLater(() -> {
            Repo repo;
            if (Arrays.asList(args).contains("--demo")) {
                repo = new MemoryRepo();
            } else {
                repo = connect();
                if (repo == null) System.exit(1);
            }
            new LoginFrame(repo).setVisible(true);
        });
    }

    private static Repo connect() {
        Properties p = new Properties();
        File f = new File("config/db.properties");
        try (FileInputStream in = new FileInputStream(f)) {
            p.load(in);
        } catch (Exception e) {
            // fall back to defaults inside JdbcRepo
        }
        JdbcRepo repo = new JdbcRepo(p);
        try {
            repo.ping();
            return repo;
        } catch (Exception e) {
            int o = JOptionPane.showConfirmDialog(null,
                "Database Connection Failed:\n" + e.getMessage()
                + "\n\nCheck config/db.properties and that MySQL is running.\nOpen in demo mode instead?",
                "Vehicle Service Management System", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            return o == JOptionPane.YES_OPTION ? new MemoryRepo() : null;
        }
    }
}
