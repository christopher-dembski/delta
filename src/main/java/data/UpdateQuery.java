package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query to update records from the database.
 *
 * @param <T> The type of object to update.
 */
public class UpdateQuery<T extends DataAccessObject> extends Query<T> {
    /**
     * The object corresponding to the record to update in the database.
     */
    private final T instance;
    /**
     * The filters applied to the query controlling which records are updated.
     */
    private final List<QueryFilter> filters;

    /**
     * @param db The database to use to execute the query.
     * @param instance The object corresponding to the record to update in the database.
     */
    protected UpdateQuery(Database db, T instance) {
        super(db, (Class<T>) instance.getClass());
        this.instance = instance;
        filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the database query specifying which records to update in the database.
     *
     * @param field              The field to filter on.
     * @param comparisonOperator The comparison to perform using the field and value as operands.
     * @param value              The value to compare the field to.
     * @return The same query instance after adding a filter to it.
     */
    public UpdateQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    /**
     * @return The object corresponding to the record to update in the database.
     */
    protected T getInstance() {
        return instance;
    }

    /**
     * Executes the query to update records in the database.
     *
     * @return True if the operation succeeded and false if the operation failed.
     */
    public boolean execute() {
        return getDatabase().executeQuery(this);
    }
}
