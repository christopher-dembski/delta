package data;

public abstract class Query<T extends DatabaseModel> {
    protected final Database db;
    protected final Class<T> klass;


    public Query(Database db, Class<T> klass) {
        this.klass = klass;
        this.db = db;
    }

    protected Class<T> getKlass() {
        return klass;
    }

    protected Database getDatabase() {
        return db;
    }
}
