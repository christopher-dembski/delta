package data;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver implements IDatabaseDriver {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/%s?user=%s&password=%s";
    private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s VALUES %s;";
    private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s WHERE id = %d;";

    private final Connection connection;

    private MySQLDriver(String databaseName, String serviceAccount, String password) throws SQLException {
        connection = DriverManager.getConnection(
                CONNECTION_STRING.formatted(databaseName, serviceAccount, password)
        );
    }

    @Override
    public boolean insert(String tableName, DatabaseRecord record) {
        // TO DO: prevent SQL injection attacks
        String insertStatement = INSERT_STATEMENT_TEMPLATE
                .formatted(tableName, MySQLDriver.convertRecordToTuple(record));
        try {
            connection.createStatement().execute(insertStatement);
        } catch (SQLException e) {
            // TO DO: implement logging to make errors easier to debug
            return false;
        }
        return true;
    }

    private static String convertRecordToTuple(DatabaseRecord record) {
        List<String> formattedValues = new ArrayList<>();
        // TO DO: use iterator pattern
        for (DatabaseValue value : record.getValues()) {
            switch (value.getType()) {
                case VARCHAR -> {
                    // need to wrap strings in single quotes
                    formattedValues.add("'%s'".formatted(value.getObject().toString()));
                }
                default -> {
                    formattedValues.add("%s".formatted(value.getObject().toString()));
                }
            }
        }
        return "(%s)".formatted(String.join(",", formattedValues));
    }

    @Override
    public boolean delete(String tableName, Integer id) {
        String deleteStatement = DELETE_STATEMENT_TEMPLATE.formatted(tableName, id);
        try {
            connection.createStatement().execute(deleteStatement);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // Example script showing how to use the MySQLDriver class
        System.out.println("Testing MySQL driver");
        List<DatabaseValue> values = new ArrayList<>();
        values.add(new DatabaseValue("id", DatabaseValueType.INTEGER, 1));
        values.add(new DatabaseValue("name", DatabaseValueType.VARCHAR, "Chris"));
        DatabaseRecord record = new DatabaseRecord(values);
        MySQLDriver instance;
        try {
            // we would define these constants in our App class
            // and have one instance for the App following the Singleton pattern
            instance = new MySQLDriver("delta_database", "delta-service-account", "password");
            instance.delete("students", 1);
            instance.insert("students", record);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
