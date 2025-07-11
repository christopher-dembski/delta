package profile.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import data.Comparison;
import data.DatabaseException;
import data.IDatabaseDriver;
import data.IRecord;
import data.InsertQuery;
import data.Record;
import data.SelectQuery;
import data.UpdateQuery;
import profile.model.Profile;
import profile.model.Sex;
import profile.model.UnitSystem;

/**
 * Adapter that maps Profile <--> data.* query objects
 * so we can delegate all SQL work to the existing MySQLDriver.
 *
 * GoF pattern applied: Adapter.
 */
public final class UserRepositoryImplementor implements UserRepository {

    private static final String TABLE = "profiles";

    private final IDatabaseDriver driver;

    /* ------------------------------------------------------------ */

    public UserRepositoryImplementor(IDatabaseDriver driver) {
        this.driver = driver;
    }

    /* ------------------------ CRUD ------------------------------ */

    @Override
    public boolean add(Profile p) {
        try {
            driver.execute(new InsertQuery(TABLE, toRecord(p)));
            return true;
        } catch (DatabaseException ex) {
            if (ex.getMessage().contains("Duplicate")) return false;
            throw new RepositoryException("insert failed", ex);
        }
    }

    @Override
    public Optional<Profile> findById(Integer id) {
        try {
            List<IRecord> rs = driver.execute(
                    new SelectQuery(TABLE).filter("id", Comparison.EQUAL, id));
            return rs.isEmpty() ? Optional.empty() : Optional.of(map(rs.getFirst()));
        } catch (DatabaseException ex) {
            throw new RepositoryException("select failed", ex);
        }
    }

    @Override
    public List<Profile> findAll() {
        try {
            List<IRecord> rs = driver.execute(new SelectQuery(TABLE));
            List<Profile> out = new ArrayList<>();
            for (IRecord r : rs) out.add(map(r));
            return out;
        } catch (DatabaseException ex) {
            throw new RepositoryException("selectAll failed", ex);
        }
    }

    @Override
    public boolean update(Profile p) {
        try {
            driver.execute(new UpdateQuery(TABLE, toRecord(p)));
            return true;
        } catch (DatabaseException ex) {
            throw new RepositoryException("update failed", ex);
        }
    }

    /* ---------------- helper mappers ---------------------------- */

    private static IRecord toRecord(Profile p) {
        Map<String, Object> m = new HashMap<>();
        if (p.getId() != null) {
            m.put("id", p.getId());
        }
        m.put("full_name",   p.getName());
        m.put("age",         p.getAge());
        m.put("sex",         p.getSex().name());
        m.put("dob",         java.sql.Date.valueOf(p.getDateOfBirth()));
        m.put("height",      p.getHeight());
        m.put("weight",      p.getWeight());
        m.put("unit_system", p.getUnitSystem().name());
        return new Record(m);
    }

    private static Profile map(IRecord r) {
        
        return new Profile.Builder()
                .id((Integer) r.getValue("id"))
                .name((String) r.getValue("full_name"))
                .age((Integer) r.getValue("age"))
                .sex(Sex.valueOf((String) r.getValue("sex")))
                .dateOfBirth(
                        ((java.sql.Date) r.getValue("dob")).toLocalDate())
                .height(((Number) r.getValue("height")).doubleValue())
                .weight(((Number) r.getValue("weight")).doubleValue())
                .unitSystem(UnitSystem.valueOf((String) r.getValue("unit_system")))
                .build();
    }

    /* custom runtime wrapper */
    public static class RepositoryException extends RuntimeException {
        public RepositoryException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
