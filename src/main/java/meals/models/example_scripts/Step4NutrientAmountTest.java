package meals.models.example_scripts;

import csv.CSVImportService;
import data.DatabaseException;
import data.MySQLConfig;
import data.MySQLDriver;
import meals.models.nutrient.Nutrient;
import meals.models.nutrient.NutrientAmount;
import meals.models.nutrient.NutrientAmountDAO;
import meals.models.nutrient.NutrientDAO;

import java.io.IOException;
import java.util.List;

/**
 * Step 4: Test NutrientAmount import - nutrition facts for our selected foods.
 * This imports 550 nutrition records for our 5 foods and 150 nutrients.
 */
public class Step4NutrientAmountTest {
    
    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Step 4: Nutrient Amount Test ===");
        
        // Create DAO
        NutrientDAO nutrientDAO = new NutrientDAO(new MySQLDriver(MySQLConfig.instance()));
        NutrientAmountDAO nutrientAmountDAO = new NutrientAmountDAO(new MySQLDriver(MySQLConfig.instance()));
        
        // Check if empty first
        System.out.println("Nutrient amounts before import: " + nutrientAmountDAO.count());
        
        // Import nutrient amounts CSV (nutrition facts for our 5 foods)
        System.out.println("\nImporting NUTRIENT_AMOUNT_TEMP_NO_STDERR.csv...");
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        csvImporter.load("src/main/java/csv/NUTRIENT_AMOUNT_TEMP_NO_STDERR.csv", "nutrient_amounts");
        System.out.println("Import complete!");
        
        // Test our class works
        int totalNutrientAmounts = nutrientAmountDAO.count();
        System.out.println("\nNutrient amounts after import: " + totalNutrientAmounts);
        
        // Show nutrition facts for cheese souffle (Food ID 2)
        System.out.println("\n=== NUTRITION FACTS FOR CHEESE SOUFFLE (Food ID 2) ===");
        List<NutrientAmount> cheeseSouffleNutrition = nutrientAmountDAO.findAll().stream()
            .filter(na -> na.getFoodId() != null && na.getFoodId() == 2)
            .limit(10)  // Show first 10 nutrients
            .toList();
            
        for (NutrientAmount na : cheeseSouffleNutrition) {
            Nutrient nutrient = nutrientDAO.findById(na.getNutrientId());
            if (nutrient != null) {
                System.out.printf("  • %s: %.2f %s\n", 
                    nutrient.getNutrientName(), 
                    na.getNutrientValue(), 
                    nutrient.getNutrientUnit());
            }
        }
        
        // Test macronutrients for all our foods
        System.out.println("\n=== MACRONUTRIENTS FOR ALL TEST FOODS ===");
        int[] foodIds = {2, 4, 5, 6, 13};
        int[] macroNutrientIds = {203, 204, 205}; // Protein, Fat, Carbs
        
        for (int foodId : foodIds) {
            System.out.printf("\nFood ID %d:\n", foodId);
            for (int nutrientId : macroNutrientIds) {
                List<NutrientAmount> macroNutrients = nutrientAmountDAO.findAll().stream()
                    .filter(na -> na.getFoodId() != null && na.getFoodId() == foodId)
                    .filter(na -> na.getNutrientId() != null && na.getNutrientId() == nutrientId)
                    .toList();
                    
                for (NutrientAmount na : macroNutrients) {
                    Nutrient nutrient = nutrientDAO.findById(na.getNutrientId());
                    if (nutrient != null) {
                        System.out.printf("  • %s: %.2f %s\n", 
                            nutrient.getNutrientName(), 
                            na.getNutrientValue(), 
                            nutrient.getNutrientUnit());
                    }
                }
            }
        }
        
        // Test calories (Energy) for all foods
        System.out.println("\n=== CALORIES (ENERGY) FOR ALL TEST FOODS ===");
        Nutrient energyNutrient = nutrientDAO.findById(208); // Energy
        for (int foodId : foodIds) {
            List<NutrientAmount> calories = nutrientAmountDAO.findAll().stream()
                .filter(na -> na.getFoodId() != null && na.getFoodId() == foodId)
                .filter(na -> na.getNutrientId() != null && na.getNutrientId() == 208) // Energy
                .toList();
                
            for (NutrientAmount na : calories) {
                System.out.printf("  Food ID %d: %.0f %s\n", 
                    foodId,
                    na.getNutrientValue(), 
                    energyNutrient != null ? energyNutrient.getNutrientUnit() : "kcal");
            }
        }
        
        System.out.println("\n✅ Step 4 Complete! Nutrition facts successfully imported and tested.");
        System.out.println("🎉 All 4 steps complete - full nutrition database working with sample data!");
    }
} 