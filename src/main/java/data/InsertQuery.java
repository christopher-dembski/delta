package data;

public class InsertQuery<T extends DataAccessObject> extends Query<T> {
    private final T instance;

    protected InsertQuery(Database db, T instance) {
        super(db, (Class<T>) instance.getClass());
        this.instance = instance;
    }

    protected T getInstance() {
        return instance;
    }

    public boolean execute() {
        return getDatabase().executeQuery(this);
    }
}
