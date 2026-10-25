package vsms;

import java.util.*;

/** In-memory demo data so the app can run without MySQL (use --demo). */
public class MemoryRepo implements Repo {
    private final Map<String, List<String[]>> data = new HashMap<>();
    private final Map<String, Integer> nextId = new HashMap<>();

    public MemoryRepo() {
        for (Spec s : Specs.ALL) { data.put(s.table, new ArrayList<>()); nextId.put(s.table, 1); }
        seed();
    }

    private void add(Spec s, String... v) {
        int id = nextId.get(s.table);
        nextId.put(s.table, id + 1);
        String[] row = new String[v.length + 1];
        row[0] = String.valueOf(id);
        System.arraycopy(v, 0, row, 1, v.length);
        data.get(s.table).add(row);
    }

    private void seed() {
        String[][] cust = {
            {"Ramesh Kumar", "9876543210", "ramesh@gmail.com", "15, Anna Nagar, Trichy - 620018", "14-05-2024"},
            {"Suresh Babu", "9123456780", "suresh@gmail.com", "12, Bharathi Street, Trichy - 620001", "13-05-2024"},
            {"Vijay Kumar", "9345678901", "vijay@gmail.com", "8, K.K. Nagar, Trichy - 620021", "12-05-2024"},
            {"Arun Prakash", "9865321470", "arun@gmail.com", "22, Woraiyur, Trichy - 620003", "11-05-2024"},
            {"Manoj Kumar", "9098765432", "manoj@gmail.com", "5, Thillai Nagar, Trichy - 620018", "10-05-2024"},
            {"Karthik B", "8833112233", "karthikb@gmail.com", "17, Thuvakudi, Trichy - 620015", "09-05-2024"},
            {"Deepak Raj", "7708996655", "deepakraj@gmail.com", "9, Melachinthamani, Trichy - 620002", "08-05-2024"},
            {"Prakash M", "9567891234", "prakashm@gmail.com", "31, Srirangam, Trichy - 620006", "07-05-2024"}};
        String[][] veh = {
            {"TN01AB1234", "Ramesh Kumar", "Car", "Maruti Suzuki Swift VDI", "Diesel", "2022", "MA3EN1ZE100123456", "D13A123456789", "14-05-2024"},
            {"TN09CD5678", "Suresh Babu", "Bike", "Honda Activa 6G", "Petrol", "2021", "MB1KW2D3S01234567", "JF50E1234567", "13-05-2024"},
            {"TN02EF9012", "Arun Prakash", "Car", "Hyundai i20 Asta", "Petrol", "2020", "MAT621GJ2N1234567", "G4LA1234567", "11-05-2024"},
            {"TN07GH3456", "Vijay Kumar", "Bike", "Yamaha FZ-S V3", "Petrol", "2021", "ME4JC75D5M1234567", "J3A1234567", "12-05-2024"},
            {"TN11IJ7890", "Manoj Kumar", "Car", "Tata Nexon XM", "Diesel", "2019", "MA1PC2EE0G1234567", "R1234567", "10-05-2024"},
            {"TN45KL2345", "Deepak Raj", "Bike", "TVS Apache RTR 160", "Petrol", "2020", "MB1HA10EZJ1234567", "HA1234567", "08-05-2024"},
            {"TN38MN6789", "Karthik B", "Car", "Kia Seltos HTK", "Diesel", "2021", "MZBCT45G0K1234567", "D4FB1234567", "09-05-2024"},
            {"TN59OP3456", "Prakash M", "Car", "Maruti Suzuki Dzire VXI", "Petrol", "2022", "MA6CB2H3XL1234567", "K12N1234567", "07-05-2024"}};
        String[][] svc = {
            {"TN01AB1234", "Ramesh Kumar", "General Service", "14-05-2024", "2500.00", "Completed", "25000", "Karthik", "Engine oil changed, air filter cleaned"},
            {"TN09CD5678", "Suresh Babu", "Washing", "13-05-2024", "500.00", "Completed", "12000", "Ravi", "Full body wash"},
            {"TN02EF9012", "Arun Prakash", "Brake Service", "13-05-2024", "1800.00", "Pending", "41000", "Karthik", "Brake pads to be replaced"},
            {"TN07GH3456", "Vijay Kumar", "Oil Change", "12-05-2024", "1200.00", "Completed", "18500", "Ravi", "Engine oil replaced"},
            {"TN11IJ7890", "Manoj Kumar", "General Service", "12-05-2024", "2000.00", "Pending", "56000", "Karthik", "Inspection scheduled"},
            {"TN45KL2345", "Deepak Raj", "Engine Repair", "11-05-2024", "5500.00", "In Progress", "33000", "Suresh", "Engine tuning"},
            {"TN38MN6789", "Karthik B", "General Service", "10-05-2024", "2200.00", "Pending", "29000", "Ravi", "Awaiting parts"},
            {"TN59OP3456", "Prakash M", "Washing", "09-05-2024", "600.00", "Completed", "8000", "Ravi", "Interior and exterior wash"}};
        String[][] pay = {
            {"INV0001", "TN01AB1234", "Ramesh Kumar", "General Service", "2500.00", "2500.00", "Paid", "Cash", "14-05-2024"},
            {"INV0002", "TN09CD5678", "Suresh Babu", "Washing", "500.00", "500.00", "Paid", "UPI", "13-05-2024"},
            {"INV0003", "TN02EF9012", "Arun Prakash", "Brake Service", "1800.00", "1000.00", "Partial", "Card", "13-05-2024"},
            {"INV0004", "TN07GH3456", "Vijay Kumar", "Oil Change", "1200.00", "1200.00", "Paid", "Cash", "12-05-2024"},
            {"INV0005", "TN11IJ7890", "Manoj Kumar", "General Service", "2000.00", "1000.00", "Partial", "UPI", "12-05-2024"},
            {"INV0006", "TN45KL2345", "Deepak Raj", "Engine Repair", "5500.00", "0.00", "Pending", "", "11-05-2024"},
            {"INV0007", "TN38MN6789", "Karthik B", "General Service", "2200.00", "0.00", "Pending", "", "10-05-2024"},
            {"INV0008", "TN59OP3456", "Prakash M", "Washing", "600.00", "600.00", "Paid", "Cash", "09-05-2024"}};
        fill(Specs.CUSTOMERS, cust); fill(Specs.VEHICLES, veh); fill(Specs.SERVICES, svc); fill(Specs.PAYMENTS, pay);
    }

    private void fill(Spec s, String[][] rows) {
        for (int i = rows.length - 1; i >= 0; i--) add(s, rows[i]);   // so the first row is the newest
    }

    @Override public List<String[]> list(Spec s, String search) {
        List<String[]> out = new ArrayList<>();
        List<String[]> rows = data.get(s.table);
        String q = search == null ? "" : search.trim().toLowerCase();
        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] r = rows.get(i);
            if (q.isEmpty() || String.join(" ", Arrays.copyOfRange(r, 1, r.length)).toLowerCase().contains(q)) out.add(r.clone());
        }
        out.sort((a, b) -> Integer.parseInt(b[0]) - Integer.parseInt(a[0]));
        return out;
    }

    @Override public void insert(Spec s, String[] v) { add(s, v.clone()); }

    @Override public void update(Spec s, String id, String[] v) {
        for (String[] r : data.get(s.table)) if (r[0].equals(id)) System.arraycopy(v, 0, r, 1, v.length);
    }

    @Override public void delete(Spec s, String id) { data.get(s.table).removeIf(r -> r[0].equals(id)); }

    @Override public long count(String table) { return data.get(table).size(); }

    @Override public double sum(String table, String col) {
        int k = Specs.byTable(table).idx(col) + 1;
        double t = 0;
        for (String[] r : data.get(table)) { try { t += Double.parseDouble(r[k]); } catch (Exception ignored) { } }
        return t;
    }

    @Override public List<String[]> serviceSummary() {
        Map<String, double[]> m = new LinkedHashMap<>();
        for (String[] r : data.get("services")) {
            double[] a = m.computeIfAbsent(r[3], k -> new double[2]);
            a[0]++;
            try { a[1] += Double.parseDouble(r[5]); } catch (Exception ignored) { }
        }
        List<String[]> out = new ArrayList<>();
        for (Map.Entry<String, double[]> e : m.entrySet())
            out.add(new String[]{e.getKey(), String.valueOf((long) e.getValue()[0]), String.format("%.2f", e.getValue()[1])});
        out.sort((a, b) -> Double.compare(Double.parseDouble(b[2]), Double.parseDouble(a[2])));
        return out;
    }

    @Override public boolean login(String u, String p) { return u.equals("admin") && p.equals("admin123"); }

    @Override public String describe() { return "Demo mode (in-memory data)"; }
}
