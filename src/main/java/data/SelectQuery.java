package data;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery<T extends DatabaseModel> {
    private final Class<T> klass;
    private final List<QueryFilter> filters;

    protected SelectQuery(Class<T> klass) {
        this.klass = klass;
        filters = new ArrayList<>();
    }

    protected Class<T> getKlass() {
        return klass;
    }

    protected SelectQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected List<QueryFilter> getFilters() {
        return filters;
    }

    protected List<T> execute() {
        return Database.executeQuery(this);
    }
}
