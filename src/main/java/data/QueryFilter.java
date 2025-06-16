package data;

public record QueryFilter(
        String field,
        ComparisonOperator comparisonOperator,
        Object value
) {
}
