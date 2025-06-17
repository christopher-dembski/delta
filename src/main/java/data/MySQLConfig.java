package data;

import java.util.HashMap;
import java.util.Map;

public class MySQLConfig {
    private final Map<String, String> tableNames;

    protected MySQLConfig() {
        tableNames = new HashMap<>();
        // class name -> table name
        tableNames.put("Student", "students");
    }

    public <T> String getTableName(Class<T> klass) {
        return tableNames.get(klass.getSimpleName());
    }
}
