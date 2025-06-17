package data;

/**
 * Represents a query to insert records into the database.
 *
 * @param <T> The type of record to insert into the database.
 */
public class InsertQuery<T extends DataAccessObject> extends Query<T> {
    /**
     * The instance to persist in the database.
     */
    private final T instance;

    /**
     * Instantiates a query to insert record into the database.
     *
     * @param db       The database to execute the query.
     * @param instance The instance to persist in the database.
     */
    protected InsertQuery(Database db, T instance) {
        super(db, (Class<T>) instance.getClass());
        this.instance = instance;
    }

    /**
     * @return The instance to persist in the database.
     */
    protected T getInstance() {
        return instance;
    }

    /**
     * Executes the query to insert records into the database.
     *
     * @return True if the operation succeeded and false if the operation failed.
     */
    public boolean execute() {
        return getDatabase().executeQuery(this);
    }
}
