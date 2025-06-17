package data;

/**
 * Abstract class handling logic common to all queries.
 *
 * @param <T> The type of object associated with the query.
 */
public abstract class Query<T extends FrozenDataAccessObject> {
    /**
     * The database to execute the query.
     */
    private final Database db;
    /**
     * The class associated with the query.
     */
    private final Class<T> klass;

    /**
     * @param db    The database used to execute the query.
     * @param klass The class associated with the database query.
     */
    public Query(Database db, Class<T> klass) {
        this.klass = klass;
        this.db = db;
    }

    /**
     * @return The class associated with the database query.
     */
    protected Class<T> getKlass() {
        return klass;
    }

    /**
     * @return The database used to execute the query.
     */
    protected Database getDatabase() {
        return db;
    }
}
