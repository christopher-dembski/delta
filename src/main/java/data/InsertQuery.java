package data;

public class InsertQuery<T extends DatabaseModel> extends Query<T> {
    private final T instance;

    protected InsertQuery(T instance) {
        super((Class<T>) instance.getClass());
        this.instance = instance;
    }

    protected T getInstance() {
        return instance;
    }

    public boolean execute() {
        return Database.executeQuery(this);
    }
}
