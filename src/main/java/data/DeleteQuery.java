package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query to delete records from a database.
 */
public class DeleteQuery extends Query {
    /**
     * A list of filters specifying which records to delete from the database.
     */
    private final List<QueryFilter> filters;

    /**
     * @param collectionName The name of the database collection to query.
     */
    public DeleteQuery(String collectionName) {
        super(collectionName);
        filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the query specifying which records to delete from the database.
     *
     * @param field      The field to filter on.
     * @param comparison The comparison to perform using the field and value as operands.
     * @param value      The value to compare the field to.
     * @return The same query instance after adding a filter to it.
     */
    public DeleteQuery filter(String field, Comparison comparison, Object value) {
        filters.add(new QueryFilter(field, comparison, value));
        return this;
    }

    /**
     * @return The list of filters applied to the query.
     */
    protected List<QueryFilter> getFilters() {
        return filters;
    }

    @Override
    public String toString() {
        return "DeleteQuery(collectionName: %s, filters: %s)"
                .formatted(getCollectionName(), getFilters());
    }
}
