package data;

public interface IDatabaseDriver {
    <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query);
    <T extends DatabaseModel> boolean executeQuery(DeleteQuery<T> query);
}
