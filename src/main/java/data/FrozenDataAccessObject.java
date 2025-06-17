package data;

/**
 * Classes implementing this interface can be read from the database.
 * Implement this method if you want to be able to query records from the database,
 * but not perform create, update, or delete operations.
 */
public interface FrozenDataAccessObject {
    /**
     * @return The id used to identify the object in the database.
     */
    Integer getId();
}
