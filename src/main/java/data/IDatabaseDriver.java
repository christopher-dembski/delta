package data;

import java.util.List;

/**
 * Defines that operation a database driver needs to implement.
 * Used to ensure the application is database agnostic.
 * For example, you could define both a MySQL driver and a Db2 driver.
 */
public interface IDatabaseDriver {
    /**
     * Executes the specified query to insert records in the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete.
     * @return True if the operation succeeded and false if the operation failed.
     */
    <T extends DataAccessObject> boolean executeQuery(InsertQuery<T> query);

    /**
     * Executes the specified query to delete records from the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to delete.
     * @return True if the operation succeeded and false if the operation failed.
     */
    <T extends DataAccessObject> boolean executeQuery(DeleteQuery<T> query);

    /**
     * Executes the specified query to update records from the database.
     *
     * @param query The query to execute.
     * @param <T>   The type of object to update.
     * @return True if the operation succeeded and false if the operation failed.
     */
    <T extends DataAccessObject> boolean executeQuery(UpdateQuery<T> query);

    <T extends FrozenDataAccessObject> List<T> executeQuery(SelectQuery<T> query);
}
