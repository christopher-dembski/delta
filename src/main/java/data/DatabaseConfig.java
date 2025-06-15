package data;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig {
    private static DatabaseConfig instance;

    private record TableConfig(String tableName) {}

    private final Map<String, TableConfig> tableConfig;

    private DatabaseConfig() {
        tableConfig = new HashMap<>();
        // TO DO: declare columns and column types here rather than in the bash script
        // and run a java function to create the tables in the database
        tableConfig.put("Student", new TableConfig("students"));
    }

    public <T> String getTableName(Class<T> klass) {
        return tableConfig.get(klass.getSimpleName()).tableName;
    }

    public static DatabaseConfig instance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
}
