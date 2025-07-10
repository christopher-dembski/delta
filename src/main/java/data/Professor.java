package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a professor entity.
 * This class implements IRecord to integrate with the database layer.
 */
public class Professor implements IRecord {
    private Integer profId;
    private String firstName;
    private String lastName;
    private String department;

    /**
     * Default constructor.
     */
    public Professor() {
    }

    /**
     * Constructor for creating a professor with all details.
     */
    public Professor(Integer profId, String firstName, String lastName, String department) {
        this.profId = profId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    /**
     * Constructor for creating a Professor from database record data.
     */
    public Professor(IRecord record) {
        this.profId = (Integer) record.getValue("prof_id");
        this.firstName = (String) record.getValue("first_name");
        this.lastName = (String) record.getValue("last_name");
        this.department = (String) record.getValue("department");
    }

    // Getters and setters
    public Integer getProfId() {
        return profId;
    }

    public void setProfId(Integer profId) {
        this.profId = profId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Domain logic methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean canTeach(String courseDepartment) {
        return this.department.equals(courseDepartment);
    }

    public String getTitle() {
        return "Prof. " + lastName;
    }

    // IRecord implementation
    @Override
    public Object getValue(String field) {
        return switch (field) {
            case "prof_id" -> profId;
            case "first_name" -> firstName;
            case "last_name" -> lastName;
            case "department" -> department;
            default -> null;
        };
    }

    @Override
    public Collection<String> fieldNames() {
        return Map.of("prof_id", profId, "first_name", firstName, 
                     "last_name", lastName, "department", department).keySet();
    }

    @Override
    public String toString() {
        return "Professor(id: %d, name: %s, department: %s)".formatted(profId, getFullName(), department);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professor professor = (Professor) obj;
        return profId != null ? profId.equals(professor.profId) : professor.profId == null;
    }

    @Override
    public int hashCode() {
        return profId != null ? profId.hashCode() : 0;
    }
} 