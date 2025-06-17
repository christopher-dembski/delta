package data;

public interface DataAccessObject extends FrozenDataAccessObject {
    DatabaseRecord toDatabaseRecord();
}
