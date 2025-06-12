package data;

public interface IDatabaseDriver {
    void insert(String tableName, DatabaseRecord record);
}
