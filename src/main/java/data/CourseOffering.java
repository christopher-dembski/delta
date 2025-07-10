package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific offering of a course in a particular semester.
 * This demonstrates COMPOSITION - CourseOffering cannot exist without its parent Course.
 * The Course owns and controls the lifecycle of its CourseOfferings.
 */
public class CourseOffering {
    private final Course parentCourse;  // Strong reference - cannot exist without Course
    private String semester;
    private String section;
    private String room;
    private String schedule;
    private List<Student> enrolledStudents;
    private int maxEnrollment;

    /**
     * Constructor requires parent Course (composition relationship).
     * CourseOffering cannot exist without a Course.
     */
    public CourseOffering(Course parentCourse, String semester, String section, String room, String schedule) {
        if (parentCourse == null) {
            throw new IllegalArgumentException("CourseOffering must have a parent Course");
        }
        this.parentCourse = parentCourse;
        this.semester = semester;
        this.section = section;
        this.room = room;
        this.schedule = schedule;
        this.enrolledStudents = new ArrayList<>();
        this.maxEnrollment = 100; // Default capacity
    }

    // Getters
    public Course getParentCourse() {
        return parentCourse;
    }

    public String getSemester() {
        return semester;
    }

    public String getSection() {
        return section;
    }

    public String getRoom() {
        return room;
    }

    public String getSchedule() {
        return schedule;
    }

    public List<Student> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents); // Return copy for encapsulation
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    // Domain logic methods using parent Course
    public String getFullCourseTitle() {
        return parentCourse.getCourseId() + " " + parentCourse.getCourseName() + 
               " (Section " + section + ")";
    }

    public String getInstructorName() {
        return parentCourse.getInstructorName();
    }

    public int getCredits() {
        return parentCourse.getCredits();
    }

    public String getDepartment() {
        return parentCourse.getDepartment();
    }

    // Enrollment management
    public boolean canEnrollStudent(Student student) {
        return !enrolledStudents.contains(student) && 
               enrolledStudents.size() < maxEnrollment &&
               student != null;
    }

    public boolean enrollStudent(Student student) {
        if (canEnrollStudent(student)) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }

    public boolean dropStudent(Student student) {
        return enrolledStudents.remove(student);
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }

    public int getAvailableSpots() {
        return maxEnrollment - getCurrentEnrollment();
    }

    public boolean isFull() {
        return getCurrentEnrollment() >= maxEnrollment;
    }

    public double getEnrollmentPercentage() {
        return (double) getCurrentEnrollment() / maxEnrollment * 100;
    }

    // Rich reporting methods
    public String getEnrollmentSummary() {
        return String.format("%s: %d/%d students enrolled (%.1f%%)", 
            getFullCourseTitle(), 
            getCurrentEnrollment(), 
            maxEnrollment, 
            getEnrollmentPercentage());
    }

    public String getOfferingDetails() {
        return String.format("""
            Course: %s
            Instructor: %s
            Section: %s
            Schedule: %s
            Room: %s
            Enrollment: %d/%d (%.1f%%)
            Department: %s
            """, 
            getFullCourseTitle(),
            getInstructorName(),
            section,
            schedule,
            room,
            getCurrentEnrollment(),
            maxEnrollment,
            getEnrollmentPercentage(),
            getDepartment());
    }

    @Override
    public String toString() {
        return "CourseOffering(" + getFullCourseTitle() + ", " + semester + 
               ", enrolled: " + getCurrentEnrollment() + "/" + maxEnrollment + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseOffering that = (CourseOffering) obj;
        return parentCourse.getCourseId().equals(that.parentCourse.getCourseId()) &&
               semester.equals(that.semester) &&
               section.equals(that.section);
    }

    @Override
    public int hashCode() {
        return (parentCourse.getCourseId() + semester + section).hashCode();
    }
} 