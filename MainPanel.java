package vsms;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Main screen content: navy sidebar + card layout of screens. */
public class MainPanel extends JPanel {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, Refreshable> screens = new LinkedHashMap<>();
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    public MainPanel(Repo repo, Runnable onLogout) {
        super(new BorderLayout());

        screens.put("Dashboard", new DashboardPanel(repo));
        screens.put("Customers", new CrudPanel(repo, Specs.CUSTOMERS));
        screens.put("Vehicles", new CrudPanel(repo, Specs.VEHICLES));
        screens.put("Service Entry", new CrudPanel(repo, Specs.SERVICES));
        screens.put("Payments", new CrudPanel(repo, Specs.PAYMENTS));
        screens.put("Reports", new ReportsPanel(repo));
        for (Map.Entry<String, Refreshable> e : screens.entrySet()) content.add((Component) e.getValue(), e.getKey());

        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Ui.NAVY);
        side.setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));
        side.setPreferredSize(new Dimension(190, 0));
        JLabel logo = new JLabel("VSMS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("<html>VEHICLE SERVICE<br>MANAGEMENT SYSTEM</html>");
        sub.setFont(Ui.BASE.deriveFont(10f));
        sub.setForeground(new Color(0xB9C7DA));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);
        side.add(sub);
        side.add(Box.createVerticalStrut(24));
        for (String name : screens.keySet()) {
            JButton b = navButton(name);
            b.addActionListener(e -> show(name));
            navButtons.put(name, b);
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }
        side.add(Box.createVerticalGlue());
        JButton out = navButton("Logout");
        out.addActionListener(e -> onLogout.run());
        side.add(out);

        JLabel status = new JLabel("  Connected: " + repo.describe());
        status.setFont(Ui.BASE.deriveFont(11f));
        status.setForeground(Ui.MUTE);
        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(side, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        show("Dashboard");
    }

    private JButton navButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(Ui.BASE);
        b.setForeground(Color.WHITE);
        b.setBackground(Ui.NAVY);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 8));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public void show(String name) {
        screens.get(name).refresh();
        cards.show(content, name);
        for (Map.Entry<String, JButton> e : navButtons.entrySet())
            e.getValue().setBackground(e.getKey().equals(name) ? Ui.BLUE : Ui.NAVY);
    }

    public java.util.Set<String> screenNames() { return screens.keySet(); }
}
