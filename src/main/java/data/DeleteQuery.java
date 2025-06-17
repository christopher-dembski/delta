package data;

import java.util.ArrayList;
import java.util.List;

public class DeleteQuery<T extends DatabaseModel> extends Query<T> {
    private final List<QueryFilter> filters;

    protected DeleteQuery(Class<T> klass) {
        super(klass);
        filters = new ArrayList<>();
    }

    protected DeleteQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected List<QueryFilter> getFilters() {
        return filters;
    }

    public boolean execute() {
        return Database.executeQuery(this);
    }
}
