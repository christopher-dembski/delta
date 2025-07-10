package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for Professor operations.
 */
public class ProfessorDAO {
    private final IDatabaseDriver driver;
    private final String tableName;

    public ProfessorDAO(IDatabaseDriver driver) {
        this.driver = driver;
        this.tableName = "professors";
    }

    public List<Professor> findAll() throws DatabaseException {
        List<IRecord> records = driver.execute(new SelectQuery(tableName));
        return records.stream()
                .map(Professor::new)
                .collect(Collectors.toList());
    }

    public Professor findById(Integer id) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("prof_id", Comparison.EQUAL, id)
        );
        return records.isEmpty() ? null : new Professor(records.get(0));
    }

    public List<Professor> findByDepartment(String department) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("department", Comparison.EQUAL, department)
        );
        return records.stream()
                .map(Professor::new)
                .collect(Collectors.toList());
    }

    public void save(Professor professor) throws DatabaseException {
        driver.execute(new InsertQuery(tableName, professor));
    }

    public void update(Professor professor) throws DatabaseException {
        driver.execute(new UpdateQuery(tableName, professor));
    }

    public void deleteById(Integer id) throws DatabaseException {
        driver.execute(
                new DeleteQuery(tableName)
                        .filter("prof_id", Comparison.EQUAL, id)
        );
    }

    public void delete(Professor professor) throws DatabaseException {
        if (professor.getProfId() != null) {
            deleteById(professor.getProfId());
        }
    }

    public int count() throws DatabaseException {
        return driver.execute(new SelectQuery(tableName)).size();
    }
} 