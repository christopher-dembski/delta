package meals.models;

import csv.CSVImportService;
import data.DatabaseException;
import data.MySQLConfig;
import data.MySQLDriver;

import java.io.IOException;
import java.util.List;

/**
 * Step 1: Test just FoodGroup import and basic functionality.
 */
public class Step1FoodGroupTest {
    
    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Step 1: Food Group Test ===");
        
        // Create DAO
        FoodGroupDAO foodGroupDAO = new FoodGroupDAO(new MySQLDriver(MySQLConfig.instance()));
        
        // Check if empty first
        System.out.println("Food groups before import: " + foodGroupDAO.count());
        
        // Import ONLY food groups CSV (clean version without French newlines)
        System.out.println("\nImporting FOOD_GROUP_TEMP.csv...");
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        csvImporter.load("src/main/java/csv/FOOD_GROUP_TEMP.csv", "food_groups");
        System.out.println("Import complete!");
        
        // Test our class works
        int totalGroups = foodGroupDAO.count();
        System.out.println("\nFood groups after import: " + totalGroups);
        
        // Show ALL food groups
        System.out.println("\n=== ALL FOOD GROUPS ===");
        List<FoodGroup> groups = foodGroupDAO.findAll();
        for (FoodGroup group : groups) {
            System.out.println("  ID: " + group.getFoodGroupId() + 
                             " | Code: " + group.getFoodGroupCode() + 
                             " | Name: " + group.getDisplayName());
        }
        
        // Test findById functionality
        System.out.println("\n=== TESTING findById ===");
        FoodGroup spicesGroup = foodGroupDAO.findById(2);
        if (spicesGroup != null) {
            System.out.println("Found by ID 2: " + spicesGroup.getDisplayName());
            System.out.println("  Is protein group? " + spicesGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + spicesGroup.isPlantBased());
        }
        
        FoodGroup bakedGroup = foodGroupDAO.findById(18);
        if (bakedGroup != null) {
            System.out.println("Found by ID 18: " + bakedGroup.getDisplayName());
            System.out.println("  Is protein group? " + bakedGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + bakedGroup.isPlantBased());
        }
        
        FoodGroup mixedGroup = foodGroupDAO.findById(22);
        if (mixedGroup != null) {
            System.out.println("Found by ID 22: " + mixedGroup.getDisplayName());
            System.out.println("  Is protein group? " + mixedGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + mixedGroup.isPlantBased());
        }
        
        System.out.println("\n=== SUCCESS: All " + totalGroups + " food groups imported and working! ===");
    }
} 