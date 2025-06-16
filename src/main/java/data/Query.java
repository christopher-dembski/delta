package data;


public abstract class Query<T extends DatabaseModel> {
    private Class<T> klass;

    protected Class<T> getKlass() {
        return klass;
    }

//    public enum QueryType {
//        INSERT,
//        SELECT,
//        UPDATE,
//        DELETE
//    }
//
//    public enum ComparisonOperator {
//        EQUAL,
//        GREATER_THAN,
//        LESS_THAN,
//        GREATER_THAN_OR_EQUAL,
//        LESS_THAN_OR_EQUAL
//    }
//
//    private record WhereClause(String field1, ComparisonOperator comparisonOperator, String field2) {}
//    private record AndClause(String field1, ComparisonOperator comparisonOperator, String field2) {}
//
//    private QueryType queryType;
//    private Class<T> klass;
//    private WhereClause whereClause;
//    private T instance;
//    private final List<AndClause> andClauses = new ArrayList<>();
//
//    // SELECT, DELETE
//    public Query(QueryType queryType, Class<T> klass) {
//        super();
//        this.queryType = queryType;
//        this.klass = klass;
//    }
//
//    // INSERT, UPDATE
//    public Query(QueryType queryType, T instance) {
//        this.queryType = queryType;
//        this.instance = instance;
//        this.klass = (Class<T>) instance.getClass();
//    }
//
//    public Query<T> where(String field1, ComparisonOperator comparisonOperator, String field2) {
//        this.whereClause = new WhereClause(field1, comparisonOperator, field2);
//        return this;
//    }
//
//    public Query<T> and(String field1, ComparisonOperator comparisonOperator, String field2) {
//        this.andClauses.add(new AndClause(field1, comparisonOperator, field2));
//        return this;
//    }
//
//    public QueryType getQueryType() {
//        return this.queryType;
//    }
//
//    public Class<T> getKlass() {
//        return this.klass;
//    }
//
//    public String tableName() {
//        return DatabaseConfig.instance().getTableName(this.klass);
//    }
//
//    public T getInstance() {
//        return this.instance;
//    }
}
