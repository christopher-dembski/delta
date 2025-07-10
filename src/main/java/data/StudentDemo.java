package data;

import java.util.List;

/**
 * Demo class showing how to use the Student class and StudentDAO.
 * This provides a much cleaner API compared to working with generic Records.
 */
public class StudentDemo {
    
    public static void main(String[] args) throws DatabaseException {
        // Create a StudentDAO instance
        StudentDAO studentDAO = new StudentDAO(new MySQLDriver(MySQLConfig.instance()));
        
        System.out.println("=== Student Demo ===");
        
        // 1. Find all students
        System.out.println("\n1. All students:");
        List<Student> allStudents = studentDAO.findAll();
        for (Student student : allStudents) {
            System.out.println("  " + student);
        }
        
        // 2. Find student by ID
        System.out.println("\n2. Find student with ID 1:");
        Student student1 = studentDAO.findById(1);
        if (student1 != null) {
            System.out.println("  Found: " + student1);
        } else {
            System.out.println("  Student with ID 1 not found");
        }
        
        // 3. Find students by name
        System.out.println("\n3. Find students named 'Chris':");
        List<Student> chrisStudents = studentDAO.findByName("Chris");
        for (Student student : chrisStudents) {
            System.out.println("  " + student);
        }
        
        // 4. Create and save a new student
        System.out.println("\n4. Creating new student:");
        Student newStudent = new Student(99, "Alice");
        System.out.println("  Created: " + newStudent);
        studentDAO.save(newStudent);
        System.out.println("  Saved to database!");
        
        // 5. Update student
        System.out.println("\n5. Updating student:");
        if (student1 != null) {
            student1.setName("Christopher");
            studentDAO.update(student1);
            System.out.println("  Updated student 1 name to: " + student1.getName());
        }
        
        // 6. Count students
        System.out.println("\n6. Total students: " + studentDAO.count());
        

        
        // 8. Final count
        System.out.println("\n8. Final count: " + studentDAO.count());
        
        System.out.println("\n=== Demo Complete ===");
    }
} 