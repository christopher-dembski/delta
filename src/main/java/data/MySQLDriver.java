package data;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An opinionated database driver for MySQL.
 * This class is a wrapper around the jdbc driver.
 * All operations on the MySQL database should go through this class.
 */
public class MySQLDriver implements IDatabaseDriver {
    /**
     * Template for establishing a database connection.
     */
    private static final String CONNECTION_STRING_TEMPLATE = "jdbc:mysql://localhost/%s?user=%s&password=%s";
    /**
     * Template for INSERT statements.
     */
    private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO %s %s VALUES %s";
    /**
     * Template for SELECT statements.
     */
    private static final String SELECT_STATEMENT_TEMPLATE = "SELECT * FROM %s";
    /**
     * Template for UPDATE statements.
     */
    private static final String UPDATE_STATEMENT_TEMPLATE = "UPDATE %s SET %s";
    /**
     * Template for DELETE statements.
     */
    private static final String DELETE_STATEMENT_TEMPLATE = "DELETE FROM %s";
    /**
     * Template for WHERE clause.
     */
    private static final String WHERE_CLAUSE_TEMPLATE = " WHERE %s %s %s";
    /**
     * Template for AND clauses.
     */
    private static final String AND_CLAUSE_TEMPLATE = " AND %s %s %s";
    /**
     * Constant representing a semicolon used to terminate SQL statements.
     */
    private static final String SEMICOLON = ";";

    /**
     * The connection to the database.
     */
    private final Connection connection;
    /**
     * The configuration for the MySQL database.
     */
    private final MySQLConfig config;

    /**
     * Return type consisting of a list of columns and their values formatted correctly to be executed in MySQL.
     *
     * @param columnNames  The column names.
     * @param columnValues The column values formatted correctly to be executed in MySQL.
     */
    private record ColumnNamesAndValues(List<String> columnNames, List<String> columnValues) {
    }

    /**
     * @param databaseName   The name of the database to execute queries against.
     * @param serviceAccount The named of the MySQL user to execute queries as.
     * @param password       The password for the MySQL database.
     * @throws SQLException Thrown if a connection to the database cannot be established.
     */
    public MySQLDriver(String databaseName, String serviceAccount, String password) throws SQLException {
        config = new MySQLConfig();
        connection = DriverManager.getConnection(
                CONNECTION_STRING_TEMPLATE.formatted(databaseName, serviceAccount, password)
        );
    }

    /**
     * Gets the column names and values formatted so that they can be executed in MySQL query.
     *
     * @param instance The instance to get the column names and values for.
     * @param <T>      The type of object to retrieve the column names and values for.
     * @return Returns the column names and their corresponding values formatted correctly for MySQL.
     */
    private static <T extends DataAccessObject> ColumnNamesAndValues getColumnNamesAndFormattedValues(T instance) {
        DatabaseRecord record = instance.toDatabaseRecord();
        List<String> formattedColumns = new ArrayList<>();
        List<String> formattedValues = new ArrayList<>();
        for (String columnName : record.getColumnNames()) {
            formattedColumns.add(columnName);
            formattedValues.add(formatSQLValue(record.getValue(columnName)));
        }
        return new ColumnNamesAndValues(formattedColumns, formattedValues);
    }

    /**
     * Executes the provided query to insert values into the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete from the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
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

    /**
     * Executes the provided query to select values into the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to select from the database.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
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

    /**
     * Executes the provided query to update records in the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to update.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
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

    /**
     * Executes the provided query to delete records from the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete.
     * @return Returns true if the operation succeeded and false if the operation failed.
     */
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

    /**
     * Builds a String representation of the WHERE / AND clauses given the filters.
     *
     * @param filters The filters applied to the query.
     * @return A String representation of the WHERE / AND clauses.
     */
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

    /**
     * Formats an object as a String, so it can be used in a MySQL query.
     * ex. String values need to wrapped in single quotes.
     *
     * @param value The value to format.
     * @return The object formatted as a String, so it can be used in a MySQL query.
     */
    private static String formatSQLValue(Object value) {
        if (value instanceof String) {
            return "'%s'".formatted(value);
        }
        return value.toString();
    }

    /**
     * Gets a String representation of a comparison operator to insert into queries.
     *
     * @param operator The comparison operator to get a String representation of.
     * @return The String representation of the comparison operator.
     */
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
                throw new RuntimeException("This operator is not supported by the MySQL driver.");
            }
        }
    }
}
