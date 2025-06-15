package data;

import java.sql.SQLException;

public class Database {
    public static IDatabaseDriver driver;

    private static final String DATABASE_NAME = "delta_database";
    private static final String DATABASE_SERVICE_ACCOUNT = "delta-service-account";
    private static final String DATABASE_SERVICE_ACCOUNT_PASSWORD = "password";

    static {
        try {
            // TO DO: throw a custom DatabaseException type in MySQLDriver constructor to make this database agnostic
            driver = new MySQLDriver(DATABASE_NAME, DATABASE_SERVICE_ACCOUNT, DATABASE_SERVICE_ACCOUNT_PASSWORD);
        } catch (SQLException e) {
            System.out.println("An error occurred when initializing the database: ");
            // for now, crash if the database fails to create a connection
            throw new RuntimeException(e);
        }
    }

    public static <T extends DatabaseModel> boolean insert(T instance) {
        Query<T> query = new Query<>(Query.QueryType.INSERT, instance);
        T result =  driver.executeQuery(query);
        return result == null;
    }

    public static boolean delete(DatabaseModel instance) {
        return driver.delete(instance.getTableName(), instance.getId());
    }

    private <T extends DatabaseModel> T executeQuery(Query<T> query) {
        return driver.executeQuery(query);
    }

    public static void main(String[] args) {
        // Example script showing how to use the ORM
        Student chris = new Student(1, "Chris");
        // Database.delete(chris);
        Database.insert(chris);
    }
}
