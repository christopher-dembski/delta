package data;

import java.util.List;

/**
 * Represents a query to insert records into the database.
 */
public class InsertQuery extends Query {
    /**
     * The instance to persist in the database.
     */
    private final List<IRecord> records;

    /**
     * @param collectionName The name of the database collection to insert the records into.
     * @param records        The records to persist in the database.
     */
    public InsertQuery(String collectionName, List<IRecord> records) {
        super(collectionName);
        this.records = records;
    }

    /**
     * @param collectionName The name of the database collection to insert the record into.
     * @param record         The record to persist in the database.
     */
    public InsertQuery(String collectionName, IRecord record) {
        this(collectionName, List.of(record));
    }

    /**
     * @return Returns the records to insert in the database.
     */
    public List<IRecord> getRecords() {
        return records;
    }

    @Override
    public String toString() {
        return "InsertQuery(collectionName: %s, records: %s)"
                .formatted(getCollectionName(), getRecords());
    }
}
