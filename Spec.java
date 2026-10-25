package vsms;

import java.util.HashMap;
import java.util.Map;

/** Describes one database table and how its form/table is shown. */
public class Spec {
    public final String title, table;
    public final String[] cols, labels;
    public final int required;                 // first N columns are mandatory
    public String[] numeric = {};              // columns that must be numbers
    public final Map<String, String[]> options = new HashMap<>(); // columns shown as drop-downs

    public Spec(String title, String table, String[] cols, String[] labels, int required) {
        this.title = title; this.table = table; this.cols = cols; this.labels = labels; this.required = required;
    }

    public int idx(String col) {
        for (int i = 0; i < cols.length; i++) if (cols[i].equals(col)) return i;
        return -1;
    }

    public boolean isNumeric(String col) {
        for (String n : numeric) if (n.equals(col)) return true;
        return false;
    }
}
