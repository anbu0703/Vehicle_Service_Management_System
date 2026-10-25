package vsms;

import java.util.List;

/** Data-access contract. JdbcRepo talks to MySQL; MemoryRepo is for demo mode. */
public interface Repo {
    /** Rows come back as [id, col1, col2, ...] (newest first). */
    List<String[]> list(Spec s, String search) throws Exception;
    void insert(Spec s, String[] values) throws Exception;
    void update(Spec s, String id, String[] values) throws Exception;
    void delete(Spec s, String id) throws Exception;
    long count(String table) throws Exception;
    double sum(String table, String col) throws Exception;
    /** Rows of [service_type, count, revenue]. */
    List<String[]> serviceSummary() throws Exception;
    boolean login(String user, String password) throws Exception;
    String describe();
}
