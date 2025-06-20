package data;

import java.util.Collection;

/**
 * Represents an entity that cam be stored in or retrieved from a database.
 */
public interface IRecord {
    /**
     * @param field The field to get the value for.
     * @return The value of the specified field.
     */
    Object getValue(String field);

    /**
     * @return A collection of unordered field names.
     */
    Collection<String> fieldNames();
}
