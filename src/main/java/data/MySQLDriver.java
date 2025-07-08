package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<IRecord> records = query.getRecords();
        // build INSERT statement
        List<String> orderedColumnNames = List.copyOf(records.getFirst().fieldNames());
        List<String> tuples = new ArrayList<>();
        for (IRecord record : records) {
            List<String> values = MySQLDriver.getColumnValuesAsStrings(orderedColumnNames, record);
            String tuple = "(%s)".formatted(String.join(", ", values));
            tuples.add(tuple);
        }
        String insertStatement = INSERT_STATEMENT_TEMPLATE.formatted(
                query.getCollectionName(),
                "(%s)".formatted(String.join(", ", orderedColumnNames)),
                String.join(", ", tuples)
        );
        insertStatement += SEMICOLON;
        // execute INSERT statement
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
        IRecord record = query.getRecord();
        // build UPDATE statement
        List<String> columnNameValuePairs = new ArrayList<>();
        List<String> orderedColumnNames = List.copyOf(record.fieldNames());
        List<String> values = MySQLDriver.getColumnValuesAsStrings(orderedColumnNames, record);
        for (int i = 0; i < orderedColumnNames.size(); i++) {
            columnNameValuePairs.add("%s = %s".formatted(orderedColumnNames.get(i), values.get(i)));
        }
        String updateStatement = UPDATE_STATEMENT_TEMPLATE.formatted(
                query.getCollectionName(),
                String.join(", ", columnNameValuePairs)
        );
        updateStatement += WHERE_CLAUSE_TEMPLATE.formatted("id", "=", record.getValue("id"));
        // execute UPDATE statement
        try {
            connection.createStatement().execute(updateStatement);
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
     * @param orderedColumnNames The column names to return the values for in the specified order.
     * @param record             The record to provide the formatted values for.
     * @return a list of values formatted as Strings that can be used in a MySQL query.
     */
    private static List<String> getColumnValuesAsStrings(List<String> orderedColumnNames, IRecord record) {
        return orderedColumnNames
                .stream()
                .map(columnName -> formatSQLValue(record.getValue(columnName)))
                .toList();
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
