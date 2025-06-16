package data;

public record QueryFilter(
        String field1,
        ComparisonOperator comparisonOperator,
        Object field2
) {
}
