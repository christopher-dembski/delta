package meals.models.example_scripts;

import csv.CSVImportService;
import data.DatabaseException;
import data.MySQLConfig;
import data.MySQLDriver;
import meals.models.nutrient.Nutrient;
import meals.models.nutrient.NutrientDAO;

import java.io.IOException;
import java.util.List;

/**
 * Step 2: Test Nutrient import and basic functionality.
 */
public class Step2NutrientTest {
    
    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Step 2: Nutrient Test ===");
        
        // Create DAO
        NutrientDAO nutrientDAO = new NutrientDAO(new MySQLDriver(MySQLConfig.instance()));
        
        // Check if empty first
        System.out.println("Nutrients before import: " + nutrientDAO.count());
        
        // Import nutrients CSV (clean version - no French fields)
        System.out.println("\nImporting NUTRIENT_NAME_TEMP.csv...");
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        csvImporter.load("src/main/java/csv/csv_files/NUTRIENT_NAME_TEMP.csv", "nutrients");
        System.out.println("Import complete!");
        
        // Test our class works
        int totalNutrients = nutrientDAO.count();
        System.out.println("\nNutrients after import: " + totalNutrients);
        
        // Show ALL nutrients
        System.out.println("\n=== ALL NUTRIENTS ===");
        List<Nutrient> nutrients = nutrientDAO.findAll();
        for (Nutrient nutrient : nutrients) {
            System.out.println("  ID: " + nutrient.getNutrientId() + 
                             " | Symbol: " + nutrient.getNutrientSymbol() + 
                             " | Name: " + nutrient.getNutrientName() + 
                             " (" + nutrient.getNutrientUnit() + ")");
        }
        
        // Test findById functionality and domain methods
        System.out.println("\n=== TESTING findById & Domain Methods ===");
        
        // Test protein (ID 203)
        Nutrient protein = nutrientDAO.findById(203);
        if (protein != null) {
            System.out.println("Found by ID 203: " + protein.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + protein.isMacronutrient());
            System.out.println("  Is vitamin? " + protein.isVitamin());
            System.out.println("  Is mineral? " + protein.isMineral());
        }
        
        // Test fat (ID 204)
        Nutrient fat = nutrientDAO.findById(204);
        if (fat != null) {
            System.out.println("Found by ID 204: " + fat.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + fat.isMacronutrient());
            System.out.println("  Is vitamin? " + fat.isVitamin());
            System.out.println("  Is mineral? " + fat.isMineral());
        }
        
        // Test carbohydrates (ID 205)
        Nutrient carbs = nutrientDAO.findById(205);
        if (carbs != null) {
            System.out.println("Found by ID 205: " + carbs.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + carbs.isMacronutrient());
            System.out.println("  Is vitamin? " + carbs.isVitamin());
            System.out.println("  Is mineral? " + carbs.isMineral());
        }
        
        // Count different types of nutrients
        System.out.println("\n=== NUTRIENT CATEGORIES ===");
        int macroCount = 0, vitaminCount = 0, mineralCount = 0;
        for (Nutrient nutrient : nutrients) {
            if (nutrient.isMacronutrient()) macroCount++;
            if (nutrient.isVitamin()) vitaminCount++;
            if (nutrient.isMineral()) mineralCount++;
        }
        System.out.println("  Macronutrients: " + macroCount);
        System.out.println("  Vitamins: " + vitaminCount);
        System.out.println("  Minerals: " + mineralCount);
        
        System.out.println("\n=== SUCCESS: All " + totalNutrients + " nutrients imported and working! ===");
    }
} 