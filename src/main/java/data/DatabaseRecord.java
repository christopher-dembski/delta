package data;

import java.util.HashMap;

public class DatabaseRecord {
    private HashMap<String, DatabaseValue> values;

    public DatabaseRecord(HashMap<String, DatabaseValue> values) {
        this.values = values;
    }

    public HashMap<String, DatabaseValue> getValues() {
        return values;
    }
}
