package data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents an entity that cam be stored in or retrieved from a database.
 */
public class Record implements IRecord {
    /**
     * Maps field names to values.
     */
    private final Map<String, Object> values;

    /**
     * Instantiates a DatabaseRecord given a map of field names and their corresponding values.
     *
     * @param values A map of field names and their corresponding values.
     */
    public Record(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Object getValue(String field) {
        return values.get(field);
    }

    @Override
    public Collection<String> fieldNames() {
        return values.keySet();
    }

    @Override
    public String toString() {
        List<String> keyValuePairs = fieldNames()
                .stream()
                .map(field -> "%s: %s".formatted(field, this.getValue(field)))
                .toList();
        return "DatabaseRecord(%s)".formatted(String.join(", ", keyValuePairs));
    }
}
