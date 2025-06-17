package data;

/**
 * Represents a filer that can be applied to a database query.
 * ex. QueryFilter("age", ComparisonOperator.GREATER_THAN_EQUAL, 65);
 *
 * @param field              The field to use for the comparison.
 * @param comparisonOperator The operator to use for the comparison.
 * @param value              The value to use for the comparison.
 */
public record QueryFilter(
        String field,
        ComparisonOperator comparisonOperator,
        Object value
) {
}
