package data;

/**
 * Abstract class handling logic common to all queries.
 */
public abstract class Query {
    /**
     * The database to execute the query.
     */
    private final String tableName;

    /**
     * @param collectionName The name of the database collection associated with the query.
     *                       In a relation database, this would be the table name.
     */
    public Query(String collectionName) {
        this.tableName = collectionName;
    }

    /**
     * @return Return the name of the database collection associated with the query.
     */
    protected String getCollectionName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "Query()";
    }
}
