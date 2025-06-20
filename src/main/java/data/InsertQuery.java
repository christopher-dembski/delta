package data;

/**
 * Represents a query to insert records into the database.
 */
public class InsertQuery extends Query {
    /**
     * The instance to persist in the database.
     */
    private final IRecord record;

    /**
     * @param collectionName The name of the database collection to insert the record into.
     * @param record         The record to persist in the database.
     */
    public InsertQuery(String collectionName, IRecord record) {
        super(collectionName);
        this.record = record;
    }

    /**
     * @return Returns the record to insert in the database.
     */
    public IRecord getRecord() {
        return record;
    }

    @Override
    public String toString() {
        return "InsertQuery(collectionName: %s, record: %s)"
                .formatted(getCollectionName(), getRecord());
    }
}
