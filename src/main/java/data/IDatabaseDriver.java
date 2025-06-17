package data;

import java.util.List;

public interface IDatabaseDriver {
    <T extends DataAccessObject> boolean executeQuery(InsertQuery<T> query);
    <T extends DataAccessObject> boolean executeQuery(DeleteQuery<T> query);
    <T extends DataAccessObject> boolean executeQuery(UpdateQuery<T> query);
    <T extends FrozenDataAccessObject> List<T> executeQuery(SelectQuery<T> query);
}
