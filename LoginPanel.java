package vsms;

import javax.swing.*;
import java.awt.*;

/** 7.1 User Login algorithm: validate fields, check credentials, open dashboard. */
public class LoginPanel extends JPanel {
    private final JTextField user = new JTextField(16);
    private final JPasswordField pass = new JPasswordField(16);
    private final JLabel msg = new JLabel(" ");

    public final JButton loginButton = Ui.button("Login", Ui.ORANGE);

    public LoginPanel(Repo repo, Runnable onSuccess) {
        super(new GridBagLayout());
        setBackground(Ui.NAVY);
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(4, 0, 4, 0); g.weightx = 1;

        JLabel title = new JLabel("VSMS Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Ui.NAVY);
        JLabel sub = new JLabel("Vehicle Service Management System", SwingConstants.CENTER);
        sub.setForeground(Ui.MUTE);
        msg.setForeground(Ui.RED);
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        JButton login = loginButton;

        g.gridy = 0; card.add(title, g);
        g.gridy = 1; card.add(sub, g);
        g.gridy = 2; g.insets = new Insets(18, 0, 2, 0); card.add(new JLabel("Username"), g);
        g.gridy = 3; g.insets = new Insets(2, 0, 2, 0); card.add(user, g);
        g.gridy = 4; g.insets = new Insets(10, 0, 2, 0); card.add(new JLabel("Password"), g);
        g.gridy = 5; g.insets = new Insets(2, 0, 2, 0); card.add(pass, g);
        g.gridy = 6; g.insets = new Insets(14, 0, 4, 0); card.add(login, g);
        g.gridy = 7; g.insets = new Insets(4, 0, 0, 0); card.add(msg, g);
        if (repo instanceof MemoryRepo) {
            JLabel hint = new JLabel("Demo login: admin / admin123", SwingConstants.CENTER);
            hint.setForeground(Ui.MUTE);
            hint.setFont(Ui.BASE.deriveFont(11f));
            g.gridy = 8; card.add(hint, g);
        }
        add(card);

        login.addActionListener(e -> {
            String u = user.getText().trim(), p = new String(pass.getPassword());
            if (u.isEmpty() || p.isEmpty()) { msg.setText("Please enter username and password."); return; }
            try {
                if (repo.login(u, p)) {
                    onSuccess.run();
                } else {
                    msg.setText("Invalid Username or Password.");
                    pass.setText("");
                }
            } catch (Exception ex) {
                msg.setText("Database error: " + ex.getMessage());
            }
        });
    }

    public void prefill(String u, String p) { user.setText(u); pass.setText(p); }
}
