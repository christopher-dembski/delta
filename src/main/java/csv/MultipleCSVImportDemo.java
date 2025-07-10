package csv;

import data.*;

import java.io.IOException;

/**
 * Demo showing how to import multiple different CSV files using the same CSVImportService.
 * This demonstrates that you DON'T need separate drivers for different CSV files.
 */
public class MultipleCSVImportDemo {
    
    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Multiple CSV Import Demo ===");
        
        // Create ONE CSVImportService instance - reuse it for all imports
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        
        // Tables are automatically created by reset-database.sh
        System.out.println("\n1. Using existing tables (courses, professors, students)...");
        
        // Import students CSV (already exists)
        System.out.println("\n2. Importing students.csv...");
        csvImporter.load("src/main/java/csv/students.csv", "students");
        System.out.println("   Students imported!");
        
        // Import courses CSV
        System.out.println("\n3. Importing courses.csv...");
        csvImporter.load("src/main/java/csv/courses.csv", "courses");
        System.out.println("   Courses imported!");
        
        // Import professors CSV
        System.out.println("\n4. Importing professors.csv...");
        csvImporter.load("src/main/java/csv/professors.csv", "professors");
        System.out.println("   Professors imported!");
        
        // Verify the imports
        System.out.println("\n5. Verifying imports:");
        IDatabaseDriver driver = new MySQLDriver(MySQLConfig.instance());
        
        int studentCount = driver.execute(new SelectQuery("students")).size();
        int courseCount = driver.execute(new SelectQuery("courses")).size();
        int professorCount = driver.execute(new SelectQuery("professors")).size();
        
        System.out.println("   Students: " + studentCount + " records");
        System.out.println("   Courses: " + courseCount + " records");
        System.out.println("   Professors: " + professorCount + " records");
        
        // Show some sample data
        System.out.println("\n6. Sample data from courses table:");
        var courses = driver.execute(new SelectQuery("courses"));
        for (var course : courses) {
            System.out.println("   Course: " + course.getValue("course_id") + 
                             " - " + course.getValue("course_name") + 
                             " (" + course.getValue("credits") + " credits)");
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Key Point: We used the SAME CSVImportService for all different CSV files!");
    }
    

} 