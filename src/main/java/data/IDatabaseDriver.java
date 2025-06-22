package data;

import java.util.List;

/**
 * Defines that operation a database driver needs to implement.
 * Used to ensure the application is database agnostic.
 * For example, you could define both a MySQL driver and a Db2 driver.
 */
public interface IDatabaseDriver {
    /**
     * Returns a configuration object mapping classes to the corresponding collection in the database.
     */
    IDatabaseConfig config();

    /**
     * Executes a query to insert a record in the database.
     *
     * @param query The query to execute.
     */
    void execute(InsertQuery query) throws DatabaseException;

    /**
     * Executes a query to delete a record from the database.
     *
     * @param query The query to execute.
     */
    void execute(DeleteQuery query) throws DatabaseException;

    /**
     * Executes a query to update a record in the database.
     *
     * @param query The query to execute.
     */
    void execute(UpdateQuery query) throws DatabaseException;

    /**
     * Executes a query to retrieve records from a database.
     *
     * @param query The query to execute.
     * @return A list of database records.
     */
    List<IRecord> execute(SelectQuery query) throws DatabaseException;
}
