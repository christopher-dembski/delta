package data;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a record persisted in a database.
 */
public class DatabaseRecord {
    /**
     * Maps column names to values.
     */
    private final Map<String, Object> values;

    /**
     * Instantiates a DatabaseRecord given a map of column names and their corresponding values.
     *
     * @param values A map of column names and their corresponding values.
     */
    public DatabaseRecord(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * Returns the value of the specified column.
     *
     * @param columnName The name of the column.
     * @return The value of the column.
     */
    public Object getValue(String columnName) {
        return values.get(columnName);
    }

    /**
     * Gets a collection of column names. Note that there is no guarantee on column ordering.
     *
     * @return A collection of column names.
     */
    public Collection<String> getColumnNames() {
        return values.keySet();
    }
}
