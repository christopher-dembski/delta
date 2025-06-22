package data;

public interface IDatabaseConfig {
    <T> String lookupCollection(Class<T> klass);
}
