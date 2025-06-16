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
    private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s";
    private static final String WHERE_CLAUSE_TEMPLATE = " WHERE %s %s %s";
    private static final String AND_CLAUSE_TEMPLATE = " AND %s %s %s";
    private static final String STATEMENT_TERMINATION_CHARACTER = ";";

    private final Connection connection;

    private record ColumnNamesAndValues(List<String> columnNames, List<String> columnValues) {
    }

    public MySQLDriver(String databaseName, String serviceAccount, String password) throws SQLException {
        connection = DriverManager.getConnection(
                CONNECTION_STRING.formatted(databaseName, serviceAccount, password)
        );
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

    // TO DO: remove, just for testing reflection
    public static <T extends DatabaseModel> T foo(Class<T> klass) {
        DatabaseRecord record = new DatabaseRecord(new HashMap<>());
        Class[] parameters = {DatabaseRecord.class};
        try {
            return klass.getDeclaredConstructor(parameters).newInstance(record);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query) {
        T instance = query.getInstance();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(instance.toDatabaseRecord());
        String columnNames = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnNames));
        String columnValues = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnValues));
        String tableName = DatabaseConfig.instance().getTableName(instance.getClass());
        String insertStatement = INSERT_STATEMENT_TEMPLATE.formatted(tableName, columnNames, columnValues);
        try {
            connection.createStatement().execute(insertStatement);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public <T extends DatabaseModel> boolean executeQuery(DeleteQuery<T> query) {
        String tableName = DatabaseConfig.instance().getTableName(query.getKlass());
        StringBuilder deleteStatement = new StringBuilder();
        deleteStatement.append(DELETE_STATEMENT_TEMPLATE.formatted(tableName));
        List<QueryFilter> filters = query.filters;
        for (int i = 0; i < filters.size(); i++) {
            QueryFilter filter = filters.get(i);
            String template = i == 0 ? WHERE_CLAUSE_TEMPLATE : AND_CLAUSE_TEMPLATE;
            deleteStatement.append(template.formatted(
                    filter.field(),
                    comparisonOperatorToString(filter.comparisonOperator()),
                    formatFilterValue(filter.value())
            ));
        }
        deleteStatement.append(STATEMENT_TERMINATION_CHARACTER);
        try {
            connection.createStatement().execute(deleteStatement.toString());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private static String formatFilterValue(Object value) {
        if (value instanceof String) {
            return "'%s'".formatted(value);
        }
        return value.toString();
    }

    private String comparisonOperatorToString(ComparisonOperator operator) {
        switch (operator) {
            case EQUAL -> {
                return "=";
            }
            case NOT_EQUAL -> {
                return "!=";
            }
            case GREATER_THAN -> {
                return ">";
            }
            case LESS_THAN -> {
                return "<";
            }
            case GREATER_THAN_EQUAL -> {
                return ">=";
            }
            case LESS_THAN_EQUAL -> {
                return "<=";
            }
            default -> {
                return "This Comparison Operator is not supported by MySQL Driver";
            }
        }
    }
}
