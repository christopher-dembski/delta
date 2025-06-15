package data;

public interface IDatabaseDriver {
    <T extends DatabaseModel> T executeQuery(Query<T> query);
    boolean insert(String tableName, DatabaseRecord record);
    boolean delete(String tableName, Integer id);
}
