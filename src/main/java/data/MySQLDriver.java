package data;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLDriver implements IDatabaseDriver {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/%s?user=%s&password=%s";
    private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s %s VALUES %s";
    private static final String SELECT_STATEMENT_TEMPLATE = "SELECT * FROM %s";
    private static final String UPDATE_STATEMENT_TEMPLATE = "UPDATE %s SET %s";
    private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s";
    private static final String WHERE_CLAUSE_TEMPLATE = " WHERE %s %s %s";
    private static final String AND_CLAUSE_TEMPLATE = " AND %s %s %s";
    private static final String SEMICOLON = ";";

    private final Connection connection;
    private final MySQLConfig config;

    private record ColumnNamesAndValues(List<String> columnNames, List<String> columnValues) {
    }

    public MySQLDriver(String databaseName, String serviceAccount, String password) throws SQLException {
        config = new MySQLConfig();
        connection = DriverManager.getConnection(
                CONNECTION_STRING.formatted(databaseName, serviceAccount, password)
        );
    }

    private static <T extends DataAccessObject> ColumnNamesAndValues getColumnNamesAndFormattedValues(T instance) {
        DatabaseRecord record = instance.toDatabaseRecord();
        List<String> formattedColumns = new ArrayList<>();
        List<String> formattedValues = new ArrayList<>();
        for (String columnName: record.getColumnNames()) {
            formattedColumns.add(columnName);
            formattedValues.add(formatSQLValue(record.getValue(columnName)));
        }
        return new ColumnNamesAndValues(formattedColumns, formattedValues);
    }

    public <T extends DataAccessObject> boolean executeQuery(InsertQuery<T> query) {
        T instance = query.getInstance();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(instance);
        String columnNames = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnNames));
        String columnValues = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnValues));
        String tableName = config.getTableName(instance.getClass());
        String insertStatement = INSERT_STATEMENT_TEMPLATE.formatted(tableName, columnNames, columnValues) + SEMICOLON;

        try {
            connection.createStatement().execute(insertStatement);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public <T extends FrozenDataAccessObject> List<T> executeQuery(SelectQuery<T> query) {
        List<T> instances = new ArrayList<>();
        Class<?>[] parameters = {DatabaseRecord.class};
        Class<T> klass = query.getKlass();
        String tableName = config.getTableName(klass);
        StringBuilder selectStatement = new StringBuilder(SELECT_STATEMENT_TEMPLATE.formatted(tableName));
        selectStatement.append(buildWhereClause(query.getFilters()));
        selectStatement.append(SEMICOLON);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(selectStatement.toString());
            ResultSetMetaData metadata = resultSet.getMetaData();
            while (resultSet.next()) {
                HashMap<String, Object> databaseValues = new HashMap<>();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    String columnName = metadata.getColumnLabel(i);
                    Object columnValue = resultSet.getObject(columnName);
                    databaseValues.put(columnName, columnValue);
                }
                DatabaseRecord record = new DatabaseRecord(databaseValues);
                // we do not know the class at compile time,
                // so we must use reflection to get the "fromDatabaseRecord" method at runtime
                instances.add((T) klass.getMethod("fromDatabaseRecord", parameters).invoke(klass, record));
            }
            return instances;
        } catch (SQLException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            return null;
        }
    }

    public <T extends DataAccessObject> boolean executeQuery(UpdateQuery<T> query) {
        T instance = query.getInstance();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(instance);
        List<String> columnNameValuePairs = new ArrayList<>();
        List<String> columnNames = columnNamesAndValues.columnNames;
        List<String> columnValues = columnNamesAndValues.columnValues;
        for (int i = 0; i < columnNames.size(); i++) {
            columnNameValuePairs.add("%s = %s".formatted(columnNames.get(i), columnValues.get(i)));
        }
        StringBuilder updateStatement = new StringBuilder(UPDATE_STATEMENT_TEMPLATE.formatted(
                config.getTableName(instance.getClass()),
                String.join(", ", columnNameValuePairs)
        ));
        updateStatement.append(WHERE_CLAUSE_TEMPLATE.formatted("id", "=", instance.getId()));
        try {
            connection.createStatement().execute(updateStatement.toString());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public <T extends DataAccessObject> boolean executeQuery(DeleteQuery<T> query) {
        String tableName = config.getTableName(query.getKlass());
        StringBuilder deleteStatement = new StringBuilder();
        deleteStatement.append(DELETE_STATEMENT_TEMPLATE.formatted(tableName));
        deleteStatement.append(buildWhereClause(query.getFilters()));
        deleteStatement.append(SEMICOLON);
        try {
            connection.createStatement().execute(deleteStatement.toString());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private String buildWhereClause(List<QueryFilter> filters) {
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < filters.size(); i++) {
            QueryFilter filter = filters.get(i);
            String template = i == 0 ? WHERE_CLAUSE_TEMPLATE : AND_CLAUSE_TEMPLATE;
            whereClause.append(template.formatted(
                    filter.field(),
                    comparisonOperatorToString(filter.comparisonOperator()),
                    formatSQLValue(filter.value())
            ));
        }
        return whereClause.toString();
    }

    private static String formatSQLValue(Object value) {
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
