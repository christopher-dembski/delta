package data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLDriver implements IDatabaseDriver {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/%s?user=%s&password=%s";
    private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s %s VALUES %s;";
    private static final String SELECT_STATEMENT_TEMPLATE = "SELECT * FROM %s";
    private static final String UPDATE_STATEMENT_TEMPLATE = "UPDATE %s SET %s";
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

    private static <T extends DatabaseModel> ColumnNamesAndValues getColumnNamesAndFormattedValues(T instance) {
        DatabaseRecord record = instance.toDatabaseRecord();
        List<String> formattedColumns = new ArrayList<>();
        List<String> formattedValues = new ArrayList<>();
        HashMap<String, DatabaseValue> columnValues = record.getValues();
        for (String columnName : columnValues.keySet()) {
            formattedColumns.add(columnName);
            DatabaseValue columnValue = columnValues.get(columnName);
            formattedValues.add(formatSQLValue(columnValue.getObject()));
        }
        return new ColumnNamesAndValues(formattedColumns, formattedValues);
    }

    public <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query) {
        T instance = query.getInstance();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(instance);
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

    public <T extends DatabaseModel> List<T> executeQuery(SelectQuery<T> query) {
        List<T> instances = new ArrayList<>();
        Class<?>[] parameters = {DatabaseRecord.class};
        Class<T> klass = query.getKlass();
        String tableName = DatabaseConfig.instance().getTableName(klass);
        StringBuilder selectStatement = new StringBuilder(SELECT_STATEMENT_TEMPLATE.formatted(tableName));
        selectStatement.append(buildWhereClause(query.getFilters()));
        selectStatement.append(STATEMENT_TERMINATION_CHARACTER);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(selectStatement.toString());
            ResultSetMetaData metadata = resultSet.getMetaData();
            while (resultSet.next()) {
                HashMap<String, DatabaseValue> databaseValues = new HashMap<>();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    String columnName = metadata.getColumnLabel(i);
                    String columnValue = resultSet.getString(columnName);
                    // TO DO: correctly identify type or remove unused DatabaseValueType
                    databaseValues.put(columnName, new DatabaseValue(columnName, DatabaseValueType.INTEGER, columnValue));
                }
                DatabaseRecord record = new DatabaseRecord(databaseValues);
                instances.add((T) klass.getMethod("fromDatabaseRecord", parameters).invoke(klass, record));
            }
            return instances;
        } catch (SQLException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            return null;
        }
    }

    public <T extends DatabaseModel> boolean executeQuery(UpdateQuery<T> query) {
        T instance = query.getInstance();
        // build string to set columns to specific values
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(instance);
        List<String> columnNameValuePairs = new ArrayList<>();
        List<String> columnNames = columnNamesAndValues.columnNames;
        List<String> columnValues = columnNamesAndValues.columnValues;
        for (int i = 0; i < columnNames.size(); i++) {
            columnNameValuePairs.add("%s = %s".formatted(columnNames.get(i), columnValues.get(i)));
        }
        StringBuilder updateStatement = new StringBuilder(UPDATE_STATEMENT_TEMPLATE.formatted(
                DatabaseConfig.instance().getTableName(instance.getClass()),
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

    public <T extends DatabaseModel> boolean executeQuery(DeleteQuery<T> query) {
        String tableName = DatabaseConfig.instance().getTableName(query.getKlass());
        StringBuilder deleteStatement = new StringBuilder();
        deleteStatement.append(DELETE_STATEMENT_TEMPLATE.formatted(tableName));
        deleteStatement.append(buildWhereClause(query.getFilters()));
        deleteStatement.append(STATEMENT_TERMINATION_CHARACTER);
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
