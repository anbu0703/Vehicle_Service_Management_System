package vsms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel implements Refreshable {
    private final Repo repo;
    private final Ui.StatCard services = new Ui.StatCard("TOTAL SERVICES", Ui.BLUE),
            revenue = new Ui.StatCard("TOTAL REVENUE", Ui.GREEN),
            customers = new Ui.StatCard("TOTAL CUSTOMERS", new Color(0x6A3FB5)),
            vehicles = new Ui.StatCard("TOTAL VEHICLES", Ui.ORANGE);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Service Type", "No. of Services", "Revenue (Rs)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public ReportsPanel(Repo repo) {
        super(new BorderLayout(0, 16));
        this.repo = repo;
        setBackground(Ui.BG);
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        add(Ui.heading("Reports"), BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 16));
        centre.setOpaque(false);
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        cards.add(services); cards.add(revenue); cards.add(customers); cards.add(vehicles);
        centre.add(cards, BorderLayout.NORTH);

        JPanel box = new JPanel(new BorderLayout(0, 8));
        box.setOpaque(false);
        JLabel l = new JLabel("Service Type Summary");
        l.setFont(Ui.BOLD.deriveFont(16f));
        l.setForeground(Ui.NAVY);
        box.add(l, BorderLayout.NORTH);
        JTable t = new JTable(model);
        Ui.styleTable(t);
        JScrollPane sp = new JScrollPane(t);
        sp.setColumnHeaderView(t.getTableHeader());
        sp.setBorder(BorderFactory.createLineBorder(Ui.BORDER));
        box.add(sp, BorderLayout.CENTER);
        centre.add(box, BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);
        refresh();
    }

    @Override public void refresh() {
        try {
            services.setValue(String.valueOf(repo.count("services")));
            revenue.setValue(String.format("Rs %,.0f", repo.sum("services", "charges")));
            customers.setValue(String.valueOf(repo.count("customers")));
            vehicles.setValue(String.valueOf(repo.count("vehicles")));
            model.setRowCount(0);
            List<String[]> rows = repo.serviceSummary();
            long n = 0; double tot = 0;
            for (String[] r : rows) {
                model.addRow(new Object[]{r[0], r[1], String.format("%,.2f", Double.parseDouble(r[2]))});
                n += Long.parseLong(r[1]);
                tot += Double.parseDouble(r[2]);
            }
            model.addRow(new Object[]{"Total", String.valueOf(n), String.format("%,.2f", tot)});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not load reports: " + ex.getMessage());
        }
    }
}
