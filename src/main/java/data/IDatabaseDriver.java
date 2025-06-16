package data;

public interface IDatabaseDriver {
    <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query);
}
