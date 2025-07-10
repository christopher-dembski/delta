package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a student entity with id and name.
 * This class implements IRecord to integrate with the database layer.
 */
public class Student implements IRecord {
    private Integer id;
    private String name;

    /**
     * Default constructor for creating a new student.
     */
    public Student() {
    }

    /**
     * Constructor for creating a student with id and name.
     *
     * @param id   The student's ID
     * @param name The student's name
     */
    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor for creating a Student from database record data.
     *
     * @param record The database record containing student data
     */
    public Student(IRecord record) {
        this.id = (Integer) record.getValue("id");
        this.name = (String) record.getValue("name");
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // IRecord implementation
    @Override
    public Object getValue(String field) {
        return switch (field) {
            case "id" -> id;
            case "name" -> name;
            default -> null;
        };
    }

    @Override
    public Collection<String> fieldNames() {
        return Map.of("id", id, "name", name).keySet();
    }

    /**
     * Converts the Student to a generic Record for database operations.
     *
     * @return A Record representing this student
     */
    public Record toRecord() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("name", name);
        return new Record(values);
    }

    @Override
    public String toString() {
        return "Student(id: %d, name: %s)".formatted(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return id != null ? id.equals(student.id) : student.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 