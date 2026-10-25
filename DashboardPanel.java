package vsms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel implements Refreshable {
    private final Repo repo;
    private final Ui.StatCard customers = new Ui.StatCard("TOTAL CUSTOMERS", Ui.BLUE),
            vehicles = new Ui.StatCard("TOTAL VEHICLES", Ui.GREEN),
            services = new Ui.StatCard("TOTAL SERVICES", Ui.ORANGE),
            payments = new Ui.StatCard("TOTAL PAYMENTS", new Color(0x6A3FB5));
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Vehicle No.", "Customer", "Service Type", "Date", "Charges (Rs)", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public DashboardPanel(Repo repo) {
        super(new BorderLayout(0, 16));
        this.repo = repo;
        setBackground(Ui.BG);
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        add(Ui.heading("Dashboard"), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 16));
        centre.setOpaque(false);
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        cards.add(customers); cards.add(vehicles); cards.add(services); cards.add(payments);
        centre.add(cards, BorderLayout.NORTH);

        JPanel recent = new JPanel(new BorderLayout(0, 8));
        recent.setOpaque(false);
        JLabel l = new JLabel("Recent Services");
        l.setFont(Ui.BOLD.deriveFont(16f));
        l.setForeground(Ui.NAVY);
        recent.add(l, BorderLayout.NORTH);
        JTable t = new JTable(model);
        Ui.styleTable(t);
        JScrollPane sp = new JScrollPane(t);
        sp.setColumnHeaderView(t.getTableHeader());
        sp.setBorder(BorderFactory.createLineBorder(Ui.BORDER));
        recent.add(sp, BorderLayout.CENTER);
        centre.add(recent, BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
        refresh();
    }

    @Override public void refresh() {
        try {
            customers.setValue(String.valueOf(repo.count("customers")));
            vehicles.setValue(String.valueOf(repo.count("vehicles")));
            services.setValue(String.valueOf(repo.count("services")));
            payments.setValue(String.format("Rs %,.0f", repo.sum("payments", "paid_amount")));
            model.setRowCount(0);
            List<String[]> rows = repo.list(Specs.SERVICES, null);
            // row = [id, vehicle_no, customer, type, date, charges, status, ...]
            for (int i = 0; i < Math.min(8, rows.size()); i++) {
                String[] r = rows.get(i);
                model.addRow(new Object[]{r[1], r[2], r[3], r[4], r[5], r[6]});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not load dashboard: " + ex.getMessage());
        }
    }
}
