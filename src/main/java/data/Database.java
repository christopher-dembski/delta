package data;

import java.sql.SQLException;
import java.util.List;

public class Database {
    public IDatabaseDriver driver;

    private static final String DATABASE_NAME = "delta_database";
    private static final String DATABASE_SERVICE_ACCOUNT = "delta-service-account";
    private static final String DATABASE_SERVICE_ACCOUNT_PASSWORD = "password";

    public Database() {
        try {
            driver = new MySQLDriver(DATABASE_NAME, DATABASE_SERVICE_ACCOUNT, DATABASE_SERVICE_ACCOUNT_PASSWORD);
        } catch (SQLException e) {
            System.out.println("An error occurred when initializing the database.");
            // TO DO: throw a custom DatabaseException type in MySQLDriver constructor to make this database agnostic
            // for now, crash if the database fails to create a connection
            throw new RuntimeException(e);
        }
    }

    public <T extends DataAccessObject> boolean insertInstance(T instance) {
        return new InsertQuery<>(this, instance).execute();
    }

    public <T extends DataAccessObject> boolean updateInstance(T instance) {
        return new UpdateQuery<T>(this, instance).execute();
    }

    public <T extends FrozenDataAccessObject> SelectQuery<T> select(Class<T> klass) {
        return new SelectQuery<>(this, klass);
    }

    public <T extends FrozenDataAccessObject> List<T> selectAll(Class<T> klass) {
        return new SelectQuery<>(this, klass).execute();
    }

    public <T extends DataAccessObject> DeleteQuery<T> delete(Class<T> klass) {
        return new DeleteQuery<>(this, klass);
    }

    public <T extends DataAccessObject> boolean deleteInstance(T instance) {
        DeleteQuery<T> query = new DeleteQuery<>(this, (Class<T>) instance.getClass());
        query = query.filter("id", ComparisonOperator.EQUAL, instance.getId());
        return query.execute();
    }

    protected <T extends DataAccessObject> boolean executeQuery(InsertQuery<T> query) {
        return driver.executeQuery(query);
    }

    protected <T extends FrozenDataAccessObject> List<T> executeQuery(SelectQuery<T> query) {
        return driver.executeQuery(query);
    }

    protected <T extends DataAccessObject> boolean executeQuery(UpdateQuery<T> query) {
        return driver.executeQuery(query);
    }

    protected <T extends DataAccessObject> boolean executeQuery(DeleteQuery<T> query) {
        return driver.executeQuery(query);
    }

    public static void main(String[] args) {
        // Example script showing how to use the ORM
        Database db = new Database();
        Student chris = new Student(1, "Chris");
        db.updateInstance(chris);
        db.deleteInstance(chris);
        db.delete(Student.class).filter("id", ComparisonOperator.EQUAL, 1).execute();
        db.insertInstance(chris);
        List<Student> students = db.selectAll(Student.class);
        for (Student student : students) {
            System.out.println(student.getName());
        }
    }
}
