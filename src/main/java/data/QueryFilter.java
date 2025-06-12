package data;

/**
 * Represents a filter that can be applied to a database query.
 * ex. new QueryFilter("age", Comparison.GREATER_THAN_EQUAL, 65);
 *
 * @param field      The field to use for the comparison.
 * @param comparison The operator to use for the comparison.
 * @param value      The value to use for the comparison.
 */
public record QueryFilter(
        String field,
        Comparison comparison,
        Object value
) {
}
