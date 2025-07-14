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
     * Optional column to sort by when executing the query.
     */
    private String sortColumn;

    /**
     * Direction to sort the results if {@code sortColumn} is set.
     */
    private Order sortDirection;

    /**
     * Optional limit for the number of returned records.
     */
    private Integer limit;

    /**
     * @param collectionName The name of the database collection to select records from.
     */
    public SelectQuery(String collectionName) {
        super(collectionName);
        filters = new ArrayList<>();
        sortColumn = null;
        sortDirection = null;
        limit = null;
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
     * Specifies a column to sort by and the direction of the sort.
     *
     * @param column The column name to sort by.
     * @param order  The order direction.
     * @return The same query instance for chaining.
     */
    public SelectQuery sort(String column, Order order) {
        this.sortColumn = column;
        this.sortDirection = order;
        return this;
    }

    /**
     * Limits the number of returned records.
     *
     * @param limit The maximum number of records to return.
     * @return The same query instance for chaining.
     */
    public SelectQuery limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * @return The list of filters applied to the query.
     */
    protected List<QueryFilter> getFilters() {
        return filters;
    }

    /**
     * @return the column to sort by or {@code null} if not specified.
     */
    protected String getSortColumn() {
        return sortColumn;
    }

    /**
     * @return the sort direction or {@code null} if not specified.
     */
    protected Order getSortDirection() {
        return sortDirection;
    }

    /**
     * @return the limit value or {@code null} if not specified.
     */
    protected Integer getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "SelectQuery(collectionName: %s, filters: %s, sortColumn: %s, sortDirection: %s, limit: %s)"
                .formatted(
                        getCollectionName(),
                        getFilters(),
                        sortColumn,
                        sortDirection,
                        limit
                );
    }
}
