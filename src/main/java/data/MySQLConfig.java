package data;

import shared.AppBackend;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the configuration for the MySQL database.
 */
public class MySQLConfig implements IDatabaseConfig {
    /**
     * A MySQLConfig instance representing the MySQL configuration for the app.
     */
    private static MySQLConfig instance;

    /**
     * The name of the host for the database connection.
     */
    private final String hostName;
    /**
     * The port number for the connection.
     */
    private final Integer portNumber;
    /**
     * The name of the database used for the application.
     */
    private final String databaseName;
    /**
     * The ID the MySQL user representing the application.
     */
    private final String serviceAccount;
    /**
     * The password for the database.
     */
    private final String serviceAccountPassword;
    /**
     * A map of class name to table names.
     * ex. {"User" -> "users"}
     */
    private final Map<String, String> tableNames;

    /**
     * @return Returns the MySQL instance representing the MySQL configuration for the app.
     */
    public static MySQLConfig instance() {
        if (instance == null) {
            instance = new MySQLConfig();
        }
        return instance;
    }

    /**
     * Instantiates the MySQL configuration.
     */
    private MySQLConfig() {
        // general setup
        String environment = AppBackend.environment();
        hostName = environment.equals(AppBackend.CI)
                ? "db"
                : "localhost";
        portNumber = 3306;
        databaseName = environment.equals(AppBackend.CI) || environment.equals(AppBackend.LOCAL_TEST)
                ? "delta_test_database"
                : "delta_database";
        serviceAccount = "delta-service-account";
        serviceAccountPassword = "password";
        // table config
        tableNames = new HashMap<>();
        // class name -> table name
        tableNames.put("Student", "students");
    }

    /**
     * Retrieves the table name where instances of the given class are persisted.
     *
     * @param klass The class to get the table name for.
     * @param <T>   The type of the class.
     * @return The name of the table for the class.
     */
    public <T> String lookupCollection(Class<T> klass) {
        return tableNames.get(klass.getSimpleName());
    }

    /**
     * @return The host name for the database connection.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return The port number of the app.
     */
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * @return The database name.
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @return The name of the MySQL user representing the app.
     */
    public String getServiceAccount() {
        return serviceAccount;
    }

    /**
     * @return The password for the MySQL user representing the app.
     */
    public String getServiceAccountPassword() {
        return serviceAccountPassword;
    }

    @Override
    public String toString() {
        return "MySQLConfig()";
    }
}
