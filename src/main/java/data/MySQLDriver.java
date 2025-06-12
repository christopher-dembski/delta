package data;

import java.util.ArrayList;
import java.util.List;

public class MySQLDriver implements IDatabaseDriver {
    private static MySQLDriver instance;

    private MySQLDriver() {
        // TO DO: establish connection
    }

    public static MySQLDriver instance() {
        if (instance == null) {
            instance = new MySQLDriver();
        }
        return instance;
    }

    @Override
    public void insert(String tableName, DatabaseRecord record) {
        List<String> formattedValues = new ArrayList<>();
        // TO DO: can use iterator pattern
        for (DatabaseValue value : record.getValues()) {
            switch (value.getType()) {
                case VARCHAR -> {
                    // need to wrap strings in single quotes
                    formattedValues.add("'%s'".formatted(value.getObject().toString()));
                }
                case INTEGER -> {
                    formattedValues.add("%s".formatted(value.getObject().toString()));
                }
            }
        }
        String statement = "INSERT INTO %s VALUES (%s);"
                .formatted(tableName, String.join(", ", formattedValues));
        System.out.println("Running statement: " + statement);
    }

    public static void main(String[] args) {
        // Example script showing how to use the MySQLDriver class
        System.out.println("Testing MySQL driver");
        List<DatabaseValue> values = new ArrayList<>();
        values.add(new DatabaseValue("id", DatabaseValueType.INTEGER, 1));
        values.add(new DatabaseValue("name", DatabaseValueType.VARCHAR, "Chris"));
        DatabaseRecord record = new DatabaseRecord(values);
        MySQLDriver instance = MySQLDriver.instance();
        instance.insert("students", record);
    }
}
