package data;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuery<T extends DatabaseModel> extends Query<T> {
    private final T instance;
    private final List<QueryFilter> filters;

    protected UpdateQuery(T instance) {
        super((Class<T>) instance.getClass());
        this.instance = instance;
        filters = new ArrayList<>();
    }

    protected UpdateQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected T getInstance() {
        return instance;
    }

    public boolean execute() {
        return Database.executeQuery(this);
    }
}
