package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the configuration for our MySQL database.
 */
public class MySQLConfig {
    /**
     * A map of class name to table names.
     * ex. {"User" -> "users"}
     */
    private final Map<String, String> tableNames;

    /**
     * Instantiates the MySQL configuration.
     */
    protected MySQLConfig() {
        tableNames = new HashMap<>();
        // class name -> table name
        tableNames.put("Student", "students");
    }

    /**
     * @param klass The class to get the table name for.
     * @param <T>   The type of the class.
     * @return The name of the table for the class.
     */
    public <T> String getTableName(Class<T> klass) {
        return tableNames.get(klass.getSimpleName());
    }
}
