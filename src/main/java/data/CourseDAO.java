package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Enhanced Data Access Object for Course operations.
 * Handles aggregation relationships with Professor.
 */
public class CourseDAO {
    private final IDatabaseDriver driver;
    private final ProfessorDAO professorDAO;
    private final String tableName;

    public CourseDAO(IDatabaseDriver driver, ProfessorDAO professorDAO) {
        this.driver = driver;
        this.professorDAO = professorDAO;
        this.tableName = "courses";
    }

    // Basic operations (without relationships)
    public List<Course> findAll() throws DatabaseException {
        List<IRecord> records = driver.execute(new SelectQuery(tableName));
        return records.stream()
                .map(Course::new)
                .collect(Collectors.toList());
    }

    public Course findById(String courseId) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("course_id", Comparison.EQUAL, courseId)
        );
        return records.isEmpty() ? null : new Course(records.get(0));
    }

    // Enhanced operations (with relationships)
    public Course findByIdWithProfessor(String courseId) throws DatabaseException {
        Course course = findById(courseId);
        if (course != null && course.getProfessorId() != null) {
            Professor professor = professorDAO.findById(course.getProfessorId());
            if (professor != null) {
                course.assignProfessor(professor);
            }
        }
        return course;
    }

    public List<Course> findAllWithProfessors() throws DatabaseException {
        List<Course> courses = findAll();
        
        // Load professors for all courses (avoiding N+1 query problem in a simple way)
        for (Course course : courses) {
            if (course.getProfessorId() != null) {
                Professor professor = professorDAO.findById(course.getProfessorId());
                if (professor != null) {
                    course.assignProfessor(professor);
                }
            }
        }
        
        return courses;
    }

    public List<Course> findByDepartment(String department) throws DatabaseException {
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("department", Comparison.EQUAL, department)
        );
        return records.stream()
                .map(Course::new)
                .collect(Collectors.toList());
    }

    public List<Course> findByDepartmentWithProfessors(String department) throws DatabaseException {
        List<Course> courses = findByDepartment(department);
        
        for (Course course : courses) {
            if (course.getProfessorId() != null) {
                Professor professor = professorDAO.findById(course.getProfessorId());
                if (professor != null) {
                    course.assignProfessor(professor);
                }
            }
        }
        
        return courses;
    }

    public void save(Course course) throws DatabaseException {
        driver.execute(new InsertQuery(tableName, course));
    }

    public void update(Course course) throws DatabaseException {
        driver.execute(new UpdateQuery(tableName, course));
    }

    public void deleteById(String courseId) throws DatabaseException {
        driver.execute(
                new DeleteQuery(tableName)
                        .filter("course_id", Comparison.EQUAL, courseId)
        );
    }

    public void delete(Course course) throws DatabaseException {
        if (course.getCourseId() != null) {
            deleteById(course.getCourseId());
        }
    }

    public int count() throws DatabaseException {
        return driver.execute(new SelectQuery(tableName)).size();
    }

    // Business logic methods
    public List<Course> findCoursesWithoutInstructor() throws DatabaseException {
        List<Course> allCourses = findAll();
        return allCourses.stream()
                .filter(course -> course.getProfessorId() == null)
                .collect(Collectors.toList());
    }

    public List<Course> findCoursesByProfessor(Integer professorId) throws DatabaseException {
        // Note: This assumes we add professor_id to the courses table
        List<IRecord> records = driver.execute(
                new SelectQuery(tableName)
                        .filter("professor_id", Comparison.EQUAL, professorId)
        );
        return records.stream()
                .map(Course::new)
                .collect(Collectors.toList());
    }
} 