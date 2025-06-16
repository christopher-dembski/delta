package data;

import java.sql.SQLException;

public class Database {
    public static IDatabaseDriver driver;

    private static final String DATABASE_NAME = "delta_database";
    private static final String DATABASE_SERVICE_ACCOUNT = "delta-service-account";
    private static final String DATABASE_SERVICE_ACCOUNT_PASSWORD = "password";

    static {
        try {
            driver = new MySQLDriver(DATABASE_NAME, DATABASE_SERVICE_ACCOUNT, DATABASE_SERVICE_ACCOUNT_PASSWORD);
        } catch (SQLException e) {
            System.out.println("An error occurred when initializing the database.");
            // TO DO: throw a custom DatabaseException type in MySQLDriver constructor to make this database agnostic
            // for now, crash if the database fails to create a connection
            throw new RuntimeException(e);
        }
    }

    public static <T extends DatabaseModel> boolean insert(T instance) {
        return executeQuery(new InsertQuery<>(instance));
    }

    public static <T extends DatabaseModel> DeleteQuery<T> delete(Class<T> klass) {
        return new DeleteQuery<>(klass);
    }

    public static <T extends DatabaseModel> boolean deleteInstance(T instance) {
        DeleteQuery<T> query = new DeleteQuery<>((Class<T>) instance.getClass());
        query = query.filter("id", ComparisonOperator.EQUAL, instance.getId());
        return query.execute();
    }

    protected static <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query) {
        return driver.executeQuery(query);
    }

    protected static <T extends DatabaseModel> boolean executeQuery(DeleteQuery<T> query) {
        return driver.executeQuery(query);
    }

    public static void main(String[] args) {
        // Example script showing how to use the ORM
        Student chris = new Student(1, "Chris");
        // boolean success = Database.deleteInstance(chris);
        // Database.delete(Student.class).filter("id", ComparisonOperator.EQUAL, 1).execute();
        Database.insert(chris);
        // Student chris = MySQLDriver.foo(Student.class);
        // System.out.println(chris.name);
    }
}
