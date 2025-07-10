package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a course entity with aggregation to Professor.
 * Demonstrates the "has-a" relationship where Course can have a Professor.
 */
public class Course implements IRecord {
    private String courseId;
    private String courseName;
    private Integer credits;
    private String department;
    
    // Aggregation - Course has a Professor (optional, professor exists independently)
    private Professor assignedProfessor;
    private Integer professorId;  // For database storage
    
    // Composition - Course owns its offerings (offerings cannot exist without course)
    private List<CourseOffering> offerings;

    /**
     * Default constructor.
     */
    public Course() {
        this.offerings = new ArrayList<>();
    }

    /**
     * Constructor for basic course without professor.
     */
    public Course(String courseId, String courseName, Integer credits, String department) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
        this.offerings = new ArrayList<>();
        this.assignedProfessor = null;
    }

    /**
     * Constructor with professor (aggregation).
     */
    public Course(String courseId, String courseName, Integer credits, String department, Professor professor) {
        this(courseId, courseName, credits, department);
        assignProfessor(professor);
    }

    /**
     * Constructor from database record.
     */
    public Course(IRecord record) {
        this.courseId = (String) record.getValue("course_id");
        this.courseName = (String) record.getValue("course_name");
        this.credits = (Integer) record.getValue("credits");
        this.department = (String) record.getValue("department");
        this.offerings = new ArrayList<>();
        this.assignedProfessor = null;
        
        // Handle professor ID if present
        Object profIdObj = record.getValue("professor_id");
        if (profIdObj instanceof Integer) {
            this.professorId = (Integer) profIdObj;
        }
    }

    // Basic getters and setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    // Aggregation methods - Managing Professor relationship
    public Professor getAssignedProfessor() {
        return assignedProfessor;
    }

    public boolean hasAssignedProfessor() {
        return assignedProfessor != null;
    }

    public String getInstructorName() {
        return hasAssignedProfessor() 
            ? assignedProfessor.getFullName() 
            : "TBA (To Be Announced)";
    }

    public void assignProfessor(Professor professor) {
        if (professor != null && canTeachCourse(professor)) {
            this.assignedProfessor = professor;
            this.professorId = professor.getProfId();
        } else if (professor != null) {
            throw new IllegalArgumentException("Professor's department (" + professor.getDepartment() + 
                ") doesn't match course department (" + this.department + ")");
        }
    }

    public void unassignProfessor() {
        this.assignedProfessor = null;
        this.professorId = null;
    }

    public boolean canTeachCourse(Professor professor) {
        return professor.canTeach(this.department);
    }

    // Composition methods - Managing CourseOfferings (owned by Course)
    public List<CourseOffering> getOfferings() {
        return new ArrayList<>(offerings);  // Return copy for encapsulation
    }

    public void addOffering(String semester, String section, String room, String schedule) {
        CourseOffering offering = new CourseOffering(this, semester, section, room, schedule);
        offerings.add(offering);
    }

    public void removeOffering(String semester, String section) {
        offerings.removeIf(offering -> 
            offering.getSemester().equals(semester) && 
            offering.getSection().equals(section));
    }

    public List<CourseOffering> getOfferingsFor(String semester) {
        return offerings.stream()
                .filter(offering -> offering.getSemester().equals(semester))
                .collect(Collectors.toList());
    }

    public boolean isOfferedIn(String semester) {
        return !getOfferingsFor(semester).isEmpty();
    }

    // Domain logic methods
    public boolean isAdvancedCourse() {
        return courseId.matches(".*[3-9]\\d{3}.*"); // 3000+ level courses
    }

    public String getCourseLevel() {
        if (courseId.matches(".*1\\d{3}.*")) return "First Year";
        if (courseId.matches(".*2\\d{3}.*")) return "Second Year";
        if (courseId.matches(".*3\\d{3}.*")) return "Third Year";
        if (courseId.matches(".*4\\d{3}.*")) return "Fourth Year";
        return "Graduate Level";
    }

    public String getFullTitle() {
        return courseId + " - " + courseName;
    }

    // IRecord implementation
    @Override
    public Object getValue(String field) {
        return switch (field) {
            case "course_id" -> courseId;
            case "course_name" -> courseName;
            case "credits" -> credits;
            case "department" -> department;
            case "professor_id" -> professorId;
            default -> null;
        };
    }

    @Override
    public Collection<String> fieldNames() {
        return Map.of("course_id", courseId, "course_name", courseName, 
                     "credits", credits, "department", department).keySet();
    }

    @Override
    public String toString() {
        return "Course(id: %s, name: %s, credits: %d, instructor: %s, offerings: %d)"
            .formatted(courseId, courseName, credits, getInstructorName(), offerings.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }

    @Override
    public int hashCode() {
        return courseId != null ? courseId.hashCode() : 0;
    }
} 