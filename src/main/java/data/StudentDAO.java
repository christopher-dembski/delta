package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for Student operations.
 * Provides convenient methods to work with Student objects and the database.
 */
public class StudentDAO {
    private final IDatabaseDriver driver;
    private final String tableName;

    /**
     * Constructor that takes a database driver.
     *
     * @param driver The database driver to use for operations
     */
    public StudentDAO(IDatabaseDriver driver) {
        this.driver = driver;
        this.tableName = "students"; // Could also use driver.config().lookupCollection(Student.class)
    }

    /**
     * Retrieves all students from the database.
     *
     * @return A list of all students
     * @throws DatabaseException if there's an error accessing the database
     */
    public List<Student> findAll() throws DatabaseException {
        List<IRecord> records = driver.execute(new SelectQuery(tableName));
        return records.stream()
                .map(Student::new)
                .collect(Collectors.toList());
    }

    /**
     * Finds a student by their ID.
     *
     * @param id The student ID to search for
     * @return The student if found, null otherwise
     * @throws DatabaseException if there's an error accessing the database
     */
    public Student findById(Integer id) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("id", Comparison.EQUAL, id)
        );
        return records.isEmpty() ? null : new Student(records.get(0));
    }

    /**
     * Finds students by name (partial match).
     *
     * @param name The name to search for
     * @return A list of students with matching names
     * @throws DatabaseException if there's an error accessing the database
     */
    public List<Student> findByName(String name) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("name", Comparison.EQUAL, name)
        );
        return records.stream()
                .map(Student::new)
                .collect(Collectors.toList());
    }

    /**
     * Saves a student to the database (insert).
     *
     * @param student The student to save
     * @throws DatabaseException if there's an error accessing the database
     */
    public void save(Student student) throws DatabaseException {
        driver.execute(new InsertQuery(tableName, student));
    }

    /**
     * Updates an existing student in the database.
     *
     * @param student The student to update
     * @throws DatabaseException if there's an error accessing the database
     */
    public void update(Student student) throws DatabaseException {
        driver.execute(new UpdateQuery(tableName, student));
    }

    /**
     * Deletes a student from the database by ID.
     *
     * @param id The ID of the student to delete
     * @throws DatabaseException if there's an error accessing the database
     */
    public void deleteById(Integer id) throws DatabaseException {
        driver.execute(
                new DeleteQuery(tableName)
                        .filter("id", Comparison.EQUAL, id)
        );
    }

    /**
     * Deletes a student from the database.
     *
     * @param student The student to delete
     * @throws DatabaseException if there's an error accessing the database
     */
    public void delete(Student student) throws DatabaseException {
        if (student.getId() != null) {
            deleteById(student.getId());
        }
    }

    /**
     * Counts the total number of students in the database.
     *
     * @return The number of students
     * @throws DatabaseException if there's an error accessing the database
     */
    public int count() throws DatabaseException {
        return driver.execute(new SelectQuery(tableName)).size();
    }
} 