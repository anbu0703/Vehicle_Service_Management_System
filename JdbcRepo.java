package vsms;

import java.sql.*;
import java.util.*;

/** MySQL implementation using plain JDBC and PreparedStatements. */
public class JdbcRepo implements Repo {
    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
    }

    private final String url, user, pass;

    public JdbcRepo(Properties p) {
        url = p.getProperty("db.url", "jdbc:mysql://localhost:3306/vsms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata");
        user = p.getProperty("db.user", "root");
        pass = p.getProperty("db.password", "");
    }

    /** Opens a connection (7.7 Database Connectivity algorithm). */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void ping() throws SQLException {
        try (Connection c = connect()) { /* connection ok */ }
    }

    @Override public List<String[]> list(Spec s, String search) throws Exception {
        String cols = String.join(",", s.cols);
        String sql = "SELECT id," + cols + " FROM " + s.table;
        boolean filter = search != null && !search.trim().isEmpty();
        if (filter) sql += " WHERE CONCAT_WS(' '," + cols + ") LIKE ?";
        sql += " ORDER BY id DESC";
        List<String[]> out = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (filter) ps.setString(1, "%" + search.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[s.cols.length + 1];
                    for (int i = 0; i < row.length; i++) {
                        String v = rs.getString(i + 1);
                        row[i] = v == null ? "" : v;
                    }
                    out.add(row);
                }
            }
        }
        return out;
    }

    @Override public void insert(Spec s, String[] v) throws Exception {
        String marks = String.join(",", Collections.nCopies(s.cols.length, "?"));
        String sql = "INSERT INTO " + s.table + "(" + String.join(",", s.cols) + ") VALUES (" + marks + ")";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, v);
            ps.executeUpdate();
        }
    }

    @Override public void update(Spec s, String id, String[] v) throws Exception {
        StringBuilder set = new StringBuilder();
        for (int i = 0; i < s.cols.length; i++) set.append(i > 0 ? "," : "").append(s.cols[i]).append("=?");
        String sql = "UPDATE " + s.table + " SET " + set + " WHERE id=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, v);
            ps.setInt(v.length + 1, Integer.parseInt(id));
            ps.executeUpdate();
        }
    }

    @Override public void delete(Spec s, String id) throws Exception {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("DELETE FROM " + s.table + " WHERE id=?")) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        }
    }

    private static void bind(PreparedStatement ps, String[] v) throws SQLException {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == null || v[i].isEmpty()) ps.setNull(i + 1, Types.VARCHAR);
            else ps.setString(i + 1, v[i]);
        }
    }

    @Override public long count(String table) throws Exception {
        try (Connection c = connect(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    @Override public double sum(String table, String col) throws Exception {
        try (Connection c = connect(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(" + col + "),0) FROM " + table)) {
            rs.next();
            return rs.getDouble(1);
        }
    }

    @Override public List<String[]> serviceSummary() throws Exception {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT service_type, COUNT(*), COALESCE(SUM(charges),0) FROM services GROUP BY service_type ORDER BY 3 DESC";
        try (Connection c = connect(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
        }
        return out;
    }

    @Override public boolean login(String u, String p) throws Exception {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE username=? AND password_hash=?")) {
            ps.setString(1, u);
            ps.setString(2, Hash.sha256(p));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    @Override public String describe() { return "MySQL (" + url.split("\\?")[0] + ")"; }
}
