package data;

/**
 * Represents an error related to creating a connection with the database or executing a query.
 */
public class DatabaseException extends Exception {
    /**
     * @param message The error message for the stack trace.
     */
    public DatabaseException(String message) {
        super(message);
    }
}
