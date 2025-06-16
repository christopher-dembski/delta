package data;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver implements IDatabaseDriver {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/%s?user=%s&password=%s";
    private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s %s VALUES %s;";
    private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s WHERE id = %d;";

    private final Connection connection;

    private record ColumnNamesAndValues(List<String> columnNames, List<String> columnValues) {}

    public MySQLDriver(String databaseName, String serviceAccount, String password) throws SQLException {
        connection = DriverManager.getConnection(
                CONNECTION_STRING.formatted(databaseName, serviceAccount, password)
        );
    }

    @Override
    public boolean insert(String tableName, DatabaseRecord record) {
        // TO DO: prevent SQL injection attacks
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(record);
        String columnNames = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnNames));
        String columnValues = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnValues));
        String insertStatement = INSERT_STATEMENT_TEMPLATE.formatted(tableName, columnNames, columnValues);
        try {
            connection.createStatement().execute(insertStatement);
        } catch (SQLException e) {
           return false;
        }
        return true;
    }

    private static ColumnNamesAndValues getColumnNamesAndFormattedValues(DatabaseRecord record) {
        List<String> formattedColumns = new ArrayList<>();
        List<String> formattedValues = new ArrayList<>();
        HashMap<String, DatabaseValue> columnValues = record.getValues();
        for (String columnName : columnValues.keySet()) {
            formattedColumns.add(columnName);
            DatabaseValue columnValue = columnValues.get(columnName);
            switch (columnValue.getType()) {
                case VARCHAR -> {
                    // need to wrap strings in single quotes
                    formattedValues.add("'%s'".formatted(columnValue.getObject().toString()));
                }
                default -> {
                    formattedValues.add("%s".formatted(columnValue.getObject().toString()));
                }
            }
        }
        return new ColumnNamesAndValues(formattedColumns, formattedValues);
    }

    // TO DO: delete
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

    // TO DO: remove, just for testing reflection
    public static <T extends DatabaseModel> T foo(Class<T> klass) {
        DatabaseRecord record = new DatabaseRecord(new HashMap<>());
        Class[] parameters = {DatabaseRecord.class};
        try {
            return klass.getDeclaredConstructor(parameters).newInstance(record);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends DatabaseModel> T executeQuery(Query<T> query) {
        switch (query.getQueryType()) {
            case INSERT -> {
                // TO DO: alter signature to pass instance rather than record
                insert(query.tableName(), query.getInstance().toDatabaseRecord());
                // TO DO: modify insert signature to return instance and null on failure
                return null;
            }
            case SELECT -> {
                return null;
            }
            case UPDATE -> {
                return null;
            }
            case DELETE -> {
                return null;
            }
        }
        return null;
    }
}
