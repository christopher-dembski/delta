package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query to select records from the database.
 */
public class SelectQuery extends Query {
    /**
     * The filters applied to the query controlling which records are selected.
     */
    private final List<QueryFilter> filters;

    /**
     * @param collectionName The name of the database collection to select records from.
     */
    public SelectQuery(String collectionName) {
        super(collectionName);
        filters = new ArrayList<>();
    }

    /**
     * Adds a filter to the query specifying which records to select from the database.
     *
     * @param field      The field to filter on.
     * @param comparison The comparison to perform using the field and value as operands.
     * @param value      The value to compare the field to.
     * @return The same query instance after adding a filter to it.
     */
    public SelectQuery filter(String field, Comparison comparison, Object value) {
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
        return "SelectQuery(collectionName: %s, filters: %s)"
                .formatted(getCollectionName(), getFilters());
    }
}
