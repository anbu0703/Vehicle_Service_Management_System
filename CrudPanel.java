package vsms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** One reusable screen: search + table + form with Add / Update / Delete / Clear. */
public class CrudPanel extends JPanel implements Refreshable {
    private final Repo repo;
    private final Spec spec;
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(18);
    private final JComponent[] fields;
    private String selectedId = null;

    public CrudPanel(Repo repo, Spec spec) {
        super(new BorderLayout(0, 12));
        this.repo = repo;
        this.spec = spec;
        setBackground(Ui.BG);
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        // top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Ui.heading(spec.title), BorderLayout.WEST);
        JPanel sp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        sp.setOpaque(false);
        JButton go = Ui.button("Search", Ui.BLUE);
        sp.add(new JLabel("Search:"));
        sp.add(search);
        sp.add(go);
        top.add(sp, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // table
        String[] heads = new String[spec.labels.length + 1];
        heads[0] = "ID";
        System.arraycopy(spec.labels, 0, heads, 1, spec.labels.length);
        model = new DefaultTableModel(heads, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        Ui.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setColumnHeaderView(table.getTableHeader());
        scroll.setBorder(BorderFactory.createLineBorder(Ui.BORDER));
        add(scroll, BorderLayout.CENTER);

        // form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Ui.BORDER), BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        fields = new JComponent[spec.cols.length];
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < spec.cols.length; i++) {
            String[] opts = spec.options.get(spec.cols[i]);
            fields[i] = opts != null ? new JComboBox<>(opts) : new JTextField(16);
            int row = i / 3, base = (i % 3) * 2;
            g.gridy = row;
            g.gridx = base;
            g.weightx = 0;
            g.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel(spec.labels[i] + (i < spec.required ? " *" : ""));
            l.setForeground(Ui.INK);
            form.add(l, g);
            g.gridx = base + 1;
            g.weightx = 1;
            g.fill = GridBagConstraints.HORIZONTAL;
            form.add(fields[i], g);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setOpaque(false);
        JButton add = Ui.button("Add", Ui.BLUE), upd = Ui.button("Update", Ui.GREEN),
                del = Ui.button("Delete", Ui.RED), clr = Ui.button("Clear", Ui.MUTE);
        buttons.add(add); buttons.add(upd); buttons.add(del); buttons.add(clr);

        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.setOpaque(false);
        south.add(form, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        // actions
        go.addActionListener(e -> refresh());
        search.addActionListener(e -> refresh());
        add.addActionListener(e -> onAdd());
        upd.addActionListener(e -> onUpdate());
        del.addActionListener(e -> onDelete());
        clr.addActionListener(e -> clearForm());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelect();
        });
        clearForm();
        refresh();
    }

    @Override public void refresh() {
        try {
            List<String[]> rows = repo.list(spec, search.getText());
            model.setRowCount(0);
            for (String[] r : rows) model.addRow(r);
        } catch (Exception ex) {
            error("Could not load " + spec.title + ": " + ex.getMessage());
        }
    }

    private String get(int i) {
        JComponent c = fields[i];
        if (c instanceof JComboBox) return String.valueOf(((JComboBox<?>) c).getSelectedItem());
        return ((JTextField) c).getText().trim();
    }

    private void set(int i, String v) {
        JComponent c = fields[i];
        if (c instanceof JComboBox) ((JComboBox<?>) c).setSelectedItem(v);
        else ((JTextField) c).setText(v);
    }

    private void clearForm() {
        selectedId = null;
        table.clearSelection();
        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof JComboBox) ((JComboBox<?>) fields[i]).setSelectedIndex(0);
            else set(i, spec.cols[i].endsWith("_date") ? today : "");
        }
    }

    private void onSelect() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        selectedId = String.valueOf(model.getValueAt(r, 0));
        for (int i = 0; i < fields.length; i++) set(i, String.valueOf(model.getValueAt(r, i + 1)));
    }

    /** Required-field and number checks (steps "validate" in the algorithms). */
    private String[] validated() {
        String[] v = new String[spec.cols.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = get(i);
            if (i < spec.required && v[i].isEmpty()) {
                error("Please fill all mandatory fields (" + spec.labels[i] + ").");
                return null;
            }
            if (spec.isNumeric(spec.cols[i]) && !v[i].isEmpty()) {
                try { Double.parseDouble(v[i]); }
                catch (NumberFormatException ex) { error(spec.labels[i] + " must be a number."); return null; }
            }
        }
        return v;
    }

    private void onAdd() {
        String[] v = validated();
        if (v == null) return;
        try {
            repo.insert(spec, v);
            info("Record added successfully.");
            clearForm();
            refresh();
        } catch (Exception ex) { error("Add failed: " + ex.getMessage()); }
    }

    private void onUpdate() {
        if (selectedId == null) { error("Select a record from the table first."); return; }
        String[] v = validated();
        if (v == null) return;
        try {
            repo.update(spec, selectedId, v);
            info("Record updated successfully.");
            clearForm();
            refresh();
        } catch (Exception ex) { error("Update failed: " + ex.getMessage()); }
    }

    private void onDelete() {
        if (selectedId == null) { error("Select a record from the table first."); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Delete the selected record permanently?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            repo.delete(spec, selectedId);
            info("Record deleted successfully.");
            clearForm();
            refresh();
        } catch (Exception ex) { error("Delete failed: " + ex.getMessage()); }
    }

    private void error(String m) { JOptionPane.showMessageDialog(this, m, "Vehicle Service Management System", JOptionPane.ERROR_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Vehicle Service Management System", JOptionPane.INFORMATION_MESSAGE); }
}
