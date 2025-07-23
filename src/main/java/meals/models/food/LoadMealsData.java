package meals.models.food;

import csv.CSVImportService;
import data.DatabaseException;
import shared.AppBackend;

import java.io.IOException;
import java.util.List;

/**
 * Load meal data - updated to use V2 classes with AppBackend.db() singleton.
 */
public class LoadMealsData {
    
    public static void main(String[] args) throws DatabaseException, IOException {
      
       // Load all the food-related data from the CSV files
       // Load the food groups
       // Load the nutrients
       // Load the foods
       // Load the nutrient amounts
       // Load the measures
       // Load the conversion factors
       try {
        System.out.println("=== Loading All Meals Data ===");
        
        System.out.println("\nImporting MEAL_LOGGING_FOOD_GROUPS.csv...");
        CSVImportService csvImporter = new CSVImportService(AppBackend.db());
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_FOOD_GROUPS.csv", "food_groups");
        System.out.println("Import complete!");
        
        System.out.println("\nImporting MEAL_LOGGING_NUTRIENTS.csv...");
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_NUTRIENTS.csv", "nutrients");
        System.out.println("Import complete!");

        System.out.println("\nImporting MEAL_LOGGING_FOODS.csv...");
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_FOODS.csv", "foods");
        System.out.println("Import complete!");
        
        System.out.println("\nImporting MEAL_LOGGING_NUTRIENT_AMOUNTS_FIXED.csv...");
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_NUTRIENT_AMOUNTS_FIXED.csv", "nutrient_amounts");
        System.out.println("Import complete!");

        System.out.println("\nImporting MEAL_LOGGING_MEASURES.csv...");
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_MEASURES.csv", "measures");
        System.out.println("Import complete!");
        
        System.out.println("\nImporting MEAL_LOGGING_CONVERSION_FACTORS.csv...");
        csvImporter.load("src/main/java/csv/MEAL_LOGGING_CONVERSION_FACTORS.csv", "conversion_factors");
        System.out.println("Import complete!");
        
        System.out.println("\n=== SUCCESS: All meals data imported! ===");
        
        // Now test our V2 classes with the imported data
        System.out.println("\n=== Testing V2 Classes ===");
        
        // Test food groups
        FoodGroupDAOV2 foodGroupDAO = new FoodGroupDAOV2();
        System.out.println("Food groups loaded: " + foodGroupDAO.count());
        
        // Test nutrients  
        NutrientDAOV2 nutrientDAO = new NutrientDAOV2();
        System.out.println("Nutrients loaded: " + nutrientDAO.count());
        
        // TODO: Test foods, nutrient amounts, measures, conversion factors
        
       } catch (Exception e) {
        System.err.println("❌ Error in loading meals data");
        e.printStackTrace();
       }
    }
}
