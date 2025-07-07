package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An opinionated database driver for MySQL.
 * This class is a simplified interface wrapping the jdbc driver.
 * All operations on the MySQL database should go through this class.
 */
public class MySQLDriver implements IDatabaseDriver {
    /**
     * Template for establishing a database connection.
     */
    private static final String CONNECTION_STRING_TEMPLATE = "jdbc:mysql://%s:%d/%s?user=%s&password=%s";
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
     * The configuration for the database.
     */
    private final MySQLConfig config;
    /**
     * The connection to the database.
     */
    private final Connection connection;

    /**
     * Return type consisting of a list of columns and their corresponding values formatted correctly to be executed in MySQL.
     *
     * @param columnNames  The column names.
     * @param columnValues The column values formatted correctly to be executed in MySQL.
     */
    private record ColumnNamesAndValues(List<String> columnNames, List<String> columnValues) {
    }

    /**
     * @throws DatabaseException Thrown if a connection to the database cannot be established.
     */
    public MySQLDriver(MySQLConfig config) throws DatabaseException {
        this.config = config;
        String connectionString = CONNECTION_STRING_TEMPLATE.formatted(
                config.getHostName(),
                config.getPortNumber(),
                config.getDatabaseName(),
                config.getServiceAccount(),
                config.getServiceAccountPassword()
        );
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to the database:\n" + e.getMessage());
        }
    }

    @Override
    public IDatabaseConfig config() {
        return config;
    }

    @Override
    public void execute(InsertQuery query) throws DatabaseException {
        IRecord IRecord = query.getRecord();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(IRecord);
        String columnNames = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnNames));
        String columnValues = "(%s)".formatted(String.join(", ", columnNamesAndValues.columnValues));
        String insertStatement = INSERT_STATEMENT_TEMPLATE.formatted(query.getCollectionName(), columnNames, columnValues) + SEMICOLON;
        try {
            connection.createStatement().execute(insertStatement);
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred while executing %s:\n%s".formatted(query, e));
        }
    }

    @Override
    public List<IRecord> execute(SelectQuery query) throws DatabaseException {
        List<IRecord> IRecords = new ArrayList<>();
        String tableName = query.getCollectionName();
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
                IRecords.add(new Record(databaseValues));
            }
            return IRecords;
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred while executing %s:\n%s".formatted(query, e));
        }
    }

    @Override
    public void execute(UpdateQuery query) throws DatabaseException {
        IRecord IRecord = query.getRecord();
        ColumnNamesAndValues columnNamesAndValues = MySQLDriver.getColumnNamesAndFormattedValues(IRecord);
        List<String> columnNameValuePairs = new ArrayList<>();
        List<String> columnNames = columnNamesAndValues.columnNames;
        List<String> columnValues = columnNamesAndValues.columnValues;
        for (int i = 0; i < columnNames.size(); i++) {
            columnNameValuePairs.add("%s = %s".formatted(columnNames.get(i), columnValues.get(i)));
        }
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append(UPDATE_STATEMENT_TEMPLATE.formatted(
                query.getCollectionName(),
                String.join(", ", columnNameValuePairs)
        ));
        updateStatement.append(WHERE_CLAUSE_TEMPLATE.formatted("id", "=", IRecord.getValue("id")));
        try {
            connection.createStatement().execute(updateStatement.toString());
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred while executing %s:\n%s".formatted(query, e));
        }
    }

    @Override
    public void execute(DeleteQuery query) throws DatabaseException {
        String tableName = query.getCollectionName();
        StringBuilder deleteStatement = new StringBuilder();
        deleteStatement.append(DELETE_STATEMENT_TEMPLATE.formatted(tableName));
        deleteStatement.append(buildWhereClause(query.getFilters()));
        deleteStatement.append(SEMICOLON);
        try {
            connection.createStatement().execute(deleteStatement.toString());
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred while executing %s:\n%s".formatted(query, e));
        }
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
                    comparisonOperatorToString(filter.comparison()),
                    formatSQLValue(filter.value())
            ));
        }
        return whereClause.toString();
    }

    /**
     * @param IRecord The column names and their corresponding values formatted correctly to be executed in MySQL.
     * @return a list of columns and their corresponding values formatted correctly to be executed in MySQL.
     */
    private static ColumnNamesAndValues getColumnNamesAndFormattedValues(IRecord IRecord) {
        List<String> formattedColumns = new ArrayList<>();
        List<String> formattedValues = new ArrayList<>();
        for (String columnName : IRecord.fieldNames()) {
            formattedColumns.add(columnName);
            formattedValues.add(formatSQLValue(IRecord.getValue(columnName)));
        }
        return new ColumnNamesAndValues(formattedColumns, formattedValues);
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
    private String comparisonOperatorToString(Comparison operator) {
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
            case GREATER_EQUAL -> {
                return ">=";
            }
            case LESS_EQUAL -> {
                return "<=";
            }
            default -> {
                throw new RuntimeException("This operator is not supported by the MySQL driver.");
            }
        }
    }

    @Override
    public String toString() {
        return "MySQLDriver()";
    }
}
