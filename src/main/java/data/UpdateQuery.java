package data;


/**
 * Represents a query to update records from the database.
 */
public class UpdateQuery extends Query {
    /**
     * The record to update in the database.
     */
    private final IRecord record;

    /**
     * @param collectionName The name of the database collection to update a record in.
     * @param record         The record to update in the database.
     */
    public UpdateQuery(String collectionName, IRecord record) {
        super(collectionName);
        this.record = record;
    }

    /**
     * @return Returns the record to update in the database.
     */
    public IRecord getRecord() {
        return record;
    }

    @Override
    public String toString() {
        return "SelectQuery(collectionName: %s, record: %s)"
                .formatted(getCollectionName(), getRecord());
    }
}
