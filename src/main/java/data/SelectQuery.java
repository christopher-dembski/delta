package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query to select records from the database.
 *
 * @param <T> The type of object to query for.
 */
public class SelectQuery<T extends FrozenDataAccessObject> extends Query<T> {
    /**
     * The filters applied to the query controlling which records are selected.
     */
    private final List<QueryFilter> filters;

    /**
     * @param db    The database used to execute the query.
     * @param klass The class to query records for.
     */
    protected SelectQuery(Database db, Class<T> klass) {
        super(db, klass);
        filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the database query specifying which records to select from the database.
     *
     * @param field              The field to filter on.
     * @param comparisonOperator The comparison to perform using the field and value as operands.
     * @param value              The value to compare the field to.
     * @return The same query instance after adding a filter to it.
     */
    public SelectQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
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
     * Executes the query to retrieve records from the database.
     *
     * @return True if the operation succeeded and false if the operation failed.
     */
    public List<T> execute() {
        return getDatabase().executeQuery(this);
    }
}
