package data;

import java.util.ArrayList;
import java.util.List;

public class DeleteQuery<T extends DataAccessObject> extends Query<T> {
    private final List<QueryFilter> filters;

    protected DeleteQuery(Database db, Class<T> klass) {
        super(db, klass);
        filters = new ArrayList<>();
    }

    public DeleteQuery<T> filter(String field, ComparisonOperator comparisonOperator, Object value) {
        filters.add(new QueryFilter(field, comparisonOperator, value));
        return this;
    }

    protected List<QueryFilter> getFilters() {
        return filters;
    }

    public boolean execute() {
        return getDatabase().executeQuery(this);
    }
}
