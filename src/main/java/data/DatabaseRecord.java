package data;

import java.util.Collection;
import java.util.HashMap;

public class DatabaseRecord {
    private final HashMap<String, Object> values;

    public DatabaseRecord(HashMap<String, Object> values) {
        this.values = values;
    }

    public Object getValue(String columnName) {
        return values.get(columnName);
    }

    public Collection<String> getColumnNames() {
        return values.keySet();
    }
}
