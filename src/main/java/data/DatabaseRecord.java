package data;

import java.util.List;

public class DatabaseRecord {
    private List<DatabaseValue> values;

    public DatabaseRecord(List<DatabaseValue> values) {
        this.values = values;
    }

    public List<DatabaseValue> getValues() {
        return values;
    }
}
