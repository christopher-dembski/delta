package data;

public class InsertQuery<T extends DatabaseModel> {
    private final T instance;

    protected InsertQuery(T instance) {
        this.instance = instance;
    }

    protected T getInstance() {
        return instance;
    }
}
