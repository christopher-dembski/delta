package data;

public interface IDatabaseDriver {
    boolean insert(String tableName, DatabaseRecord record);
    boolean delete(String tableName, Integer id);
}
