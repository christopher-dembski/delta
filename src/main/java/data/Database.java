package data;

import java.sql.SQLException;
import java.util.List;

/**
 * Class that enables performing CRUD operations on a database.
 */
public class Database {
    /**
     * The database driver used for executing queries.
     */
    public IDatabaseDriver driver;

    /**
     * The name of the database used for the application.
     */
    private static final String DATABASE_NAME = "delta_database";
    /**
     * The name of the user to perform database queries.
     */
    private static final String DATABASE_SERVICE_ACCOUNT = "delta-service-account";
    /**
     * The password for the database.
     */
    private static final String DATABASE_SERVICE_ACCOUNT_PASSWORD = "password";

    /**
     * Creates a new database instance using the default settings.
     */
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

    /**
     * Adds an entry for the specified object into the database.
     *
     * @param instance The object to insert into the database.
     * @param <T>      The type of the object to insert into the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    public <T extends DataAccessObject> boolean insertInstance(T instance) {
        return new InsertQuery<>(this, instance).execute();
    }

    /**
     * Updates the entry in the database corresponding to this object.
     *
     * @param instance The object to insert into the database.
     * @param <T>      The type of the object to insert into the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    public <T extends DataAccessObject> boolean updateInstance(T instance) {
        return new UpdateQuery<T>(this, instance).execute();
    }

    /**
     * Build a query to select records from the database.
     *
     * @param klass The class of the object to query for.
     * @param <T>   The type of object to query for.
     * @return Returns a query to select the specified records from the database.
     */
    public <T extends FrozenDataAccessObject> SelectQuery<T> select(Class<T> klass) {
        return new SelectQuery<>(this, klass);
    }

    /**
     * The class to select all records for.
     *
     * @param klass The class of the object to query for.
     * @param <T>   The type of object to query for.
     * @return Returns all objects of the specified class stored in the database.
     */
    public <T extends FrozenDataAccessObject> List<T> selectAll(Class<T> klass) {
        return new SelectQuery<>(this, klass).execute();
    }

    /**
     * Builds a query to delete records from the database.
     *
     * @param klass The class to delete records for.
     * @param <T>   The type of the class to delete records for.
     * @return Returns a query to delete records from the database.
     */
    public <T extends DataAccessObject> DeleteQuery<T> delete(Class<T> klass) {
        return new DeleteQuery<>(this, klass);
    }

    /**
     * Deletes the specified object from the database.
     *
     * @param instance The object to delete from the database.
     * @param <T>      The type of object to delete from the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    public <T extends DataAccessObject> boolean deleteInstance(T instance) {
        DeleteQuery<T> query = new DeleteQuery<>(this, (Class<T>) instance.getClass());
        query = query.filter("id", ComparisonOperator.EQUAL, instance.getId());
        return query.execute();
    }

    /**
     * Executes the provided query to insert values into the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete from the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    protected <T extends DataAccessObject> boolean executeQuery(InsertQuery<T> query) {
        return driver.executeQuery(query);
    }

    /**
     * Executes the provided query to select values into the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to select from the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    protected <T extends FrozenDataAccessObject> List<T> executeQuery(SelectQuery<T> query) {
        return driver.executeQuery(query);
    }

    /**
     * Executes the provided query to update records in the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to update.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    protected <T extends DataAccessObject> boolean executeQuery(UpdateQuery<T> query) {
        return driver.executeQuery(query);
    }

    /**
     * Executes the provided query to delete records from the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
    protected <T extends DataAccessObject> boolean executeQuery(DeleteQuery<T> query) {
        return driver.executeQuery(query);
    }

    /**
     * Example main method to demonstrate the data platform's ability to perform CRUD operations.
     *
     * @param args Command line arguments.
     */
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
