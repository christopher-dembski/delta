package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query to delete records from a database.
 *
 * @param <T> The type of the object to delete records for.
 */
public class DeleteQuery<T extends DataAccessObject> extends Query<T> {
    /**
     * A list of filters specifying which records to delete from the database.
     */
    private final List<QueryFilter> filters;

    /**
     * Instantiates a query to delete records from the database.
     *
     * @param db    The database used to execute the query.
     * @param klass The class of the object to delete from the database.
     */
    protected DeleteQuery(Database db, Class<T> klass) {
        super(db, klass);
        filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the database query specifying which records to delete from the database.
     *
     * @param field              The field to filter on.
     * @param comparisonOperator The comparison to perform using the field and value as operands.
     * @param value              The value to compare the field to.
     * @return The same query instance after adding a filter to it.
     */
    public DeleteQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    /**
     * @return The list of filters applied to the query.
     */
    protected List<QueryFilter> getFilters() {
        return filters;
    }

    /**
     * Executes the query to delete records from the database.
     *
     * @return True if the operation succeeded and false if the operation failed.
     */
    public boolean execute() {
        return getDatabase().executeQuery(this);
    }
}
