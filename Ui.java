package vsms;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/** Shared colours, fonts and small widget helpers. */
public final class Ui {
    private Ui() {}

    public static final Color NAVY = new Color(0x0B2545), ORANGE = new Color(0xE8761A), BG = new Color(0xF2F6FB),
            INK = new Color(0x1B2733), MUTE = new Color(0x5C6B7A), BORDER = new Color(0xDCE4EE),
            GREEN = new Color(0x1E8449), RED = new Color(0xB03A2E), BLUE = new Color(0x1F5FBF);
    public static final Font BASE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font BOLD = BASE.deriveFont(Font.BOLD);
    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 22);

    public static void install() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) { }
        for (Object k : UIManager.getLookAndFeelDefaults().keySet().toArray()) {
            if (k.toString().endsWith(".font")) UIManager.put(k, BASE);
        }
    }

    public static JButton button(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(BOLD);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(BASE);
        t.setForeground(INK);
        t.setGridColor(BORDER);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(0xD6E4F7));
        t.setSelectionForeground(INK);
        t.setFillsViewportHeight(true);
        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(0xE9EFF7));
        h.setForeground(NAVY);
        h.setFont(BOLD);
        h.setReorderingAllowed(false);
    }

    /** Big-number summary card. */
    public static class StatCard extends JPanel {
        private final JLabel value = new JLabel("0");

        public StatCard(String caption, Color accent) {
            super(new BorderLayout(0, 4));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(14, 18, 14, 18)));
            value.setFont(new Font("SansSerif", Font.BOLD, 28));
            value.setForeground(accent);
            JLabel cap = new JLabel(caption);
            cap.setFont(BASE);
            cap.setForeground(MUTE);
            add(value, BorderLayout.CENTER);
            add(cap, BorderLayout.SOUTH);
        }

        public void setValue(String v) { value.setText(v); }
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(TITLE);
        l.setForeground(NAVY);
        return l;
    }
}
