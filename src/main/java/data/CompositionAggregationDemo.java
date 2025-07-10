package data;

import java.util.List;

/**
 * Comprehensive demo showing Aggregation and Composition patterns.
 * 
 * AGGREGATION: Course "has-a" Professor (Professor exists independently)
 * COMPOSITION: Course "owns" CourseOfferings (CourseOffering cannot exist without Course)
 */
public class CompositionAggregationDemo {
    
    public static void main(String[] args) throws DatabaseException {
        System.out.println("=== Composition & Aggregation Demo ===");
        
        // Set up DAOs
        IDatabaseDriver driver = new MySQLDriver(MySQLConfig.instance());
        ProfessorDAO professorDAO = new ProfessorDAO(driver);
        CourseDAO courseDAO = new CourseDAO(driver, professorDAO);
        StudentDAO studentDAO = new StudentDAO(driver);
        
        // Clean demo - demonstrate aggregation and composition
        demonstrateAggregation(courseDAO, professorDAO);
        demonstrateComposition();
        demonstrateRichDomainLogic(courseDAO, professorDAO, studentDAO);
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void demonstrateAggregation(CourseDAO courseDAO, ProfessorDAO professorDAO) throws DatabaseException {
        System.out.println("\n🔗 AGGREGATION DEMO (Course has-a Professor)");
        System.out.println("Professor exists independently of Course\n");
        
        // Create independent professors
        Professor drSmith = new Professor(1, "John", "Smith", "Computer Science");
        Professor drJohnson = new Professor(2, "Sarah", "Johnson", "Computer Science");
        Professor drBrown = new Professor(3, "Michael", "Brown", "Mathematics");
        
        System.out.println("1. Created independent professors:");
        System.out.println("   " + drSmith);
        System.out.println("   " + drJohnson);
        System.out.println("   " + drBrown);
        
        // Create courses and demonstrate aggregation
        Course softwareDesign = new Course("EECS3311", "Software Design", 3, "Computer Science");
        Course dataStructures = new Course("EECS2030", "Advanced OOP", 3, "Computer Science");
        Course calculus = new Course("MATH1090", "Calculus I", 4, "Mathematics");
        
        System.out.println("\n2. Created courses without professors:");
        System.out.println("   " + softwareDesign);
        System.out.println("   " + dataStructures);
        System.out.println("   " + calculus);
        
        // Demonstrate aggregation - assigning professors to courses
        System.out.println("\n3. Demonstrating Aggregation (assigning professors):");
        
        softwareDesign.assignProfessor(drSmith);
        System.out.println("   ✅ Assigned " + drSmith.getFullName() + " to " + softwareDesign.getCourseId());
        
        dataStructures.assignProfessor(drJohnson);
        System.out.println("   ✅ Assigned " + drJohnson.getFullName() + " to " + dataStructures.getCourseId());
        
        calculus.assignProfessor(drBrown);
        System.out.println("   ✅ Assigned " + drBrown.getFullName() + " to " + calculus.getCourseId());
        
        // Show courses with professors
        System.out.println("\n4. Courses with assigned professors:");
        System.out.println("   " + softwareDesign);
        System.out.println("   " + dataStructures);
        System.out.println("   " + calculus);
        
        // Demonstrate department validation (business rule)
        System.out.println("\n5. Testing department validation:");
        try {
            softwareDesign.assignProfessor(drBrown); // Math prof teaching CS course
        } catch (IllegalArgumentException e) {
            System.out.println("   ❌ Cannot assign Math professor to CS course: " + e.getMessage());
        }
        
        // Demonstrate professor independence (aggregation characteristic)
        System.out.println("\n6. Demonstrating Professor independence:");
        System.out.println("   Before unassigning: " + softwareDesign.getInstructorName());
        softwareDesign.unassignProfessor();
        System.out.println("   After unassigning: " + softwareDesign.getInstructorName());
        System.out.println("   Professor still exists: " + drSmith + " ✅");
    }
    
    private static void demonstrateComposition() {
        System.out.println("\n🏗️ COMPOSITION DEMO (Course owns CourseOfferings)");
        System.out.println("CourseOffering cannot exist without parent Course\n");
        
        // Create a course with professor
        Professor drSmith = new Professor(1, "John", "Smith", "Computer Science");
        Course softwareDesign = new Course("EECS3311", "Software Design", 3, "Computer Science", drSmith);
        
        System.out.println("1. Created course: " + softwareDesign);
        
        // Demonstrate composition - Course owns and creates CourseOfferings
        System.out.println("\n2. Adding CourseOfferings (Composition):");
        
        softwareDesign.addOffering("Fall 2024", "A", "CB 122", "MWF 10:00-11:00");
        softwareDesign.addOffering("Fall 2024", "B", "CB 123", "TTh 14:30-16:00");
        softwareDesign.addOffering("Winter 2025", "A", "CB 124", "MWF 13:00-14:00");
        
        System.out.println("   ✅ Added 3 offerings to " + softwareDesign.getCourseId());
        
        // Show all offerings
        System.out.println("\n3. All offerings for " + softwareDesign.getCourseId() + ":");
        for (CourseOffering offering : softwareDesign.getOfferings()) {
            System.out.println("   " + offering);
        }
        
        // Demonstrate filtering by semester
        System.out.println("\n4. Fall 2024 offerings only:");
        for (CourseOffering offering : softwareDesign.getOfferingsFor("Fall 2024")) {
            System.out.println("   " + offering);
        }
        
        // Demonstrate composition dependency
        System.out.println("\n5. Demonstrating Composition dependency:");
        System.out.println("   Course offerings count: " + softwareDesign.getOfferings().size());
        System.out.println("   Is offered in Fall 2024? " + softwareDesign.isOfferedIn("Fall 2024"));
        System.out.println("   Is offered in Summer 2024? " + softwareDesign.isOfferedIn("Summer 2024"));
        
        // Try to create orphaned CourseOffering (should fail)
        System.out.println("\n6. Testing composition constraint:");
        try {
            new CourseOffering(null, "Fall 2024", "C", "CB 125", "Online");
        } catch (IllegalArgumentException e) {
            System.out.println("   ❌ Cannot create CourseOffering without parent Course: " + e.getMessage());
        }
    }
    
    private static void demonstrateRichDomainLogic(CourseDAO courseDAO, ProfessorDAO professorDAO, StudentDAO studentDAO) throws DatabaseException {
        System.out.println("\n🎯 RICH DOMAIN LOGIC DEMO");
        System.out.println("Showing business methods enabled by composition/aggregation\n");
        
        // Set up rich objects
        Professor drSmith = new Professor(1, "John", "Smith", "Computer Science");
        Course softwareDesign = new Course("EECS3311", "Software Design", 3, "Computer Science", drSmith);
        
        // Add offering with composition
        softwareDesign.addOffering("Fall 2024", "A", "CB 122", "MWF 10:00-11:00");
        CourseOffering offering = softwareDesign.getOfferingsFor("Fall 2024").get(0);
        
        // Create students for enrollment
        Student alice = new Student(1, "Alice Johnson");
        Student bob = new Student(2, "Bob Smith");
        Student charlie = new Student(3, "Charlie Brown");
        
        System.out.println("1. Course and offering setup:");
        System.out.println("   " + softwareDesign);
        System.out.println("   " + offering);
        
        // Demonstrate rich enrollment logic
        System.out.println("\n2. Student enrollment (using composition):");
        System.out.println("   Initial enrollment: " + offering.getCurrentEnrollment() + "/" + offering.getMaxEnrollment());
        
        offering.enrollStudent(alice);
        offering.enrollStudent(bob);
        offering.enrollStudent(charlie);
        
        System.out.println("   After enrolling 3 students: " + offering.getCurrentEnrollment() + "/" + offering.getMaxEnrollment());
        System.out.println("   Enrollment percentage: " + String.format("%.1f%%", offering.getEnrollmentPercentage()));
        
        // Rich reporting using both aggregation and composition
        System.out.println("\n3. Rich domain reporting:");
        System.out.println("   Course level: " + softwareDesign.getCourseLevel());
        System.out.println("   Is advanced course? " + softwareDesign.isAdvancedCourse());
        System.out.println("   Instructor: " + softwareDesign.getInstructorName());
        System.out.println("   Full title: " + softwareDesign.getFullTitle());
        
        System.out.println("\n4. Detailed offering information:");
        System.out.println(offering.getOfferingDetails());
        
        // Business logic across relationships
        System.out.println("5. Cross-relationship business logic:");
        System.out.println("   Can Dr. Smith teach this course? " + softwareDesign.canTeachCourse(drSmith));
        System.out.println("   Available spots: " + offering.getAvailableSpots());
        System.out.println("   Is course full? " + offering.isFull());
        
        Professor mathProf = new Professor(4, "Alice", "Wilson", "Mathematics");
        System.out.println("   Can Math prof teach CS course? " + softwareDesign.canTeachCourse(mathProf));
    }
} 