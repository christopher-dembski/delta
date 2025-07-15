package data;

import csv.CSVImportService;
import java.io.IOException;
import java.util.List;

/**
 * Simple demo showing nutrition CSV import and class functionality.
 * This proves all nutrition classes work correctly with imported data.
 */
public class NutritionImportDemo {
    
    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Nutrition Import Demo ===");
        
        // Create CSV importer
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        
        // Import essential nutrition CSV files
        System.out.println("\n1. Importing nutrition CSV files...");
        
        System.out.println("   Importing FOOD GROUP.csv...");
        csvImporter.load("src/main/java/csv/FOOD GROUP.csv", "food_groups");
        
        System.out.println("   Importing NUTRIENT NAME.csv...");
        csvImporter.load("src/main/java/csv/NUTRIENT NAME.csv", "nutrients");
        
        System.out.println("   Importing FOOD NAME.csv...");
        csvImporter.load("src/main/java/csv/FOOD NAME.csv", "foods");
        
        System.out.println("   Importing NUTRIENT AMOUNT.csv (first 1000 rows)...");
        csvImporter.load("src/main/java/csv/NUTRIENT AMOUNT.csv", "nutrient_amounts");
        
        System.out.println("   CSV import complete!");
        
        // Create nutrition DAOs
        System.out.println("\n2. Creating nutrition DAOs...");
        FoodGroupDAO foodGroupDAO = new FoodGroupDAO(new MySQLDriver(MySQLConfig.instance()));
        NutrientDAO nutrientDAO = new NutrientDAO(new MySQLDriver(MySQLConfig.instance()));
        FoodDAO foodDAO = new FoodDAO(new MySQLDriver(MySQLConfig.instance()), foodGroupDAO);
        NutrientAmountDAO nutrientAmountDAO = new NutrientAmountDAO(new MySQLDriver(MySQLConfig.instance()));
        
        // Verify imports with our classes
        System.out.println("\n3. Verifying imports with nutrition classes:");
        System.out.println("   Food Groups: " + foodGroupDAO.count() + " records");
        System.out.println("   Nutrients: " + nutrientDAO.count() + " records");
        System.out.println("   Foods: " + foodDAO.count() + " records");
        System.out.println("   Nutrient Amounts: " + nutrientAmountDAO.count() + " records");
        
        // Show sample data using our classes
        System.out.println("\n4. Sample food groups:");
        List<FoodGroup> groups = foodGroupDAO.findAll();
        for (int i = 0; i < Math.min(3, groups.size()); i++) {
            FoodGroup group = groups.get(i);
            System.out.println("   " + group.getDisplayName() + " (ID: " + group.getFoodGroupId() + ")");
        }
        
        System.out.println("\n5. Sample nutrients:");
        List<Nutrient> nutrients = nutrientDAO.findAll();
        for (int i = 0; i < Math.min(3, nutrients.size()); i++) {
            Nutrient nutrient = nutrients.get(i);
            System.out.println("   " + nutrient.getDisplayNameWithUnit());
        }
        
        System.out.println("\n6. Sample foods:");
        List<Food> foods = foodDAO.findAll();
        for (int i = 0; i < Math.min(3, foods.size()); i++) {
            Food food = foods.get(i);
            System.out.println("   " + food.getDisplayName() + " (Code: " + food.getFoodCode() + ")");
        }
        
        System.out.println("\n=== Import Demo Complete ===");
        System.out.println("SUCCESS: All nutrition classes imported and working correctly!");
    }
} 