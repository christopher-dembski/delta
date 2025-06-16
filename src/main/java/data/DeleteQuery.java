package data;

import java.util.ArrayList;
import java.util.List;

public class DeleteQuery<T extends DatabaseModel> {
    private final Class<T> klass;
    private List<QueryFilter> filters;

    protected DeleteQuery(Class<T> klass) {
        this.klass = klass;
        filters = new ArrayList<>();
    }

    protected Class<T> getKlass() {
        return klass;
    }

    protected DeleteQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected List<QueryFilter> getFilters() {
        return filters;
    }

    protected boolean execute() {
        return Database.executeQuery(this);
    }
}
