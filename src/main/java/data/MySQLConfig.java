package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the configuration for the MySQL database.
 */
public class MySQLConfig {
    /**
     * A MySQLConfig instance representing the MySQL configuration for the app.
     */
    private static MySQLConfig instance;

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
    protected static MySQLConfig instance() {
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
        databaseName = "delta_database";
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
    public <T> String lookupTableName(Class<T> klass) {
        return tableNames.get(klass.getSimpleName());
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
