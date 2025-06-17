package data;

public abstract class Query<T extends DatabaseModel> {
    protected final Class<T> klass;

    public Query(Class<T> klass) {
        this.klass = klass;
    }

    protected Class<T> getKlass() {
        return klass;
    }
}
