package data;

/**
 * Classes implementing this interface can have CRUD database operations performed on them.
 */
public interface DataAccessObject extends FrozenDataAccessObject {
    /**
     *
     * @return A representation of the object in a format that can be saved to the database.
     */
    DatabaseRecord toDatabaseRecord();
}
