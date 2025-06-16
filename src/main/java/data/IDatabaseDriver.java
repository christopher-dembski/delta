package data;

import java.util.List;

public interface IDatabaseDriver {
    <T extends DatabaseModel> boolean executeQuery(InsertQuery<T> query);
    <T extends DatabaseModel> boolean executeQuery(DeleteQuery<T> query);
    <T extends DatabaseModel> boolean executeQuery(UpdateQuery<T> query);
    <T extends DatabaseModel> List<T> executeQuery(SelectQuery<T> query);
}
