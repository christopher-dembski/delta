package data;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery<T extends DatabaseModel> extends Query<T> {
    private final List<QueryFilter> filters;

    protected SelectQuery(Class<T> klass) {
        super(klass);
        filters = new ArrayList<>();
    }

    public SelectQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected List<QueryFilter> getFilters() {
        return filters;
    }

    public List<T> execute() {
        return Database.executeQuery(this);
    }
}
