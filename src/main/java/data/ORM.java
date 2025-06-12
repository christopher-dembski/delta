package data;

import java.sql.SQLException;

public class ORM {
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

    public static boolean insert(DatabaseModel instance) {
        return driver.insert(instance.getTableName(), instance.accept(ToDatabaseRecordVisitor.instance()));
    }

    public static boolean delete(DatabaseModel instance) {
        return driver.delete(instance.getTableName(), instance.getId());
    }

    public static void main(String[] args) {
        // Example script showing how to use the ORM
        Student chris = new Student(1, "Chris");
        ORM.delete(chris);
        ORM.insert(chris);
    }
}
