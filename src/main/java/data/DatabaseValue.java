package data;

public class DatabaseValue {
    private final String columnName;
    private final DatabaseValueType type;
    private final Object object;

    public DatabaseValue(String columnName, DatabaseValueType type, Object value) {
        this.columnName = columnName;
        this.type = type;
        this.object = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public DatabaseValueType getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
