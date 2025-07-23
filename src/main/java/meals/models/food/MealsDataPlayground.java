package meals.models.food;

import data.DatabaseException;
import java.io.IOException;
import java.util.List;

/**
 * Playground for testing V2 meal classes without re-loading data.
 * Assumes data has already been loaded via LoadMealsData.java
 */
public class MealsDataPlayground {

    public static void main(String[] args) throws DatabaseException, IOException {
        System.out.println("=== Meals Data Playground (V2 Classes) ===");

        // Test FoodGroupV2 and FoodGroupDAOV2
        System.out.println("\n=== Testing FoodGroupV2 ===");
        FoodGroupDAOV2 foodGroupDAO = new FoodGroupDAOV2();

        int totalGroups = foodGroupDAO.count();
        System.out.println("Food groups in database: " + totalGroups);

        // Show ALL food groups using V2
        System.out.println("\n=== ALL FOOD GROUPS (V2) ===");
        List<FoodGroupV2> groups = foodGroupDAO.findAll();
        for (FoodGroupV2 group : groups) {
            System.out.println("  ID: " + group.getFoodGroupId() +
                    " | Code: " + group.getFoodGroupCode() +
                    " | Name: " + group.getDisplayName());
        }

        // Test findById functionality - using meal logging food groups
        System.out.println("\n=== TESTING findById (V2) ===");
        FoodGroupV2 dairyGroup = foodGroupDAO.findById(1);
        if (dairyGroup != null) {
            System.out.println("Found by ID 1: " + dairyGroup.getDisplayName());
            System.out.println("  Is protein group? " + dairyGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + dairyGroup.isPlantBased());
        }

        FoodGroupV2 beverageGroup = foodGroupDAO.findById(14);
        if (beverageGroup != null) {
            System.out.println("Found by ID 14: " + beverageGroup.getDisplayName());
            System.out.println("  Is protein group? " + beverageGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + beverageGroup.isPlantBased());
        }

        FoodGroupV2 mixedGroup = foodGroupDAO.findById(22);
        if (mixedGroup != null) {
            System.out.println("Found by ID 22: " + mixedGroup.getDisplayName());
            System.out.println("  Is protein group? " + mixedGroup.isProteinGroup());
            System.out.println("  Is plant-based? " + mixedGroup.isPlantBased());
        }

        // Test NutrientV2 and NutrientDAOV2  
        System.out.println("\n=== Testing NutrientV2 ===");
        NutrientDAOV2 nutrientDAO = new NutrientDAOV2();

        int totalNutrients = nutrientDAO.count();
        System.out.println("Nutrients in database: " + totalNutrients);

        // Show ALL nutrients using V2
        System.out.println("\n=== ALL NUTRIENTS (V2) ===");
        List<NutrientV2> nutrients = nutrientDAO.findAll();
        for (NutrientV2 nutrient : nutrients) {
            System.out.println("  ID: " + nutrient.getNutrientId() +
                    " | Symbol: " + nutrient.getNutrientSymbol() +
                    " | Name: " + nutrient.getNutrientName() +
                    " (" + nutrient.getNutrientUnit() + ")");
        }

        // Test specific nutrients
        System.out.println("\n=== TESTING Key Nutrients & Domain Methods (V2) ===");

        // Test protein (ID 203)
        NutrientV2 protein = nutrientDAO.findById(203);
        if (protein != null) {
            System.out.println("Found by ID 203: " + protein.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + protein.isMacronutrient());
            System.out.println("  Is vitamin? " + protein.isVitamin());
            System.out.println("  Is mineral? " + protein.isMineral());
        }

        // Test fat (ID 204)
        NutrientV2 fat = nutrientDAO.findById(204);
        if (fat != null) {
            System.out.println("Found by ID 204: " + fat.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + fat.isMacronutrient());
            System.out.println("  Is vitamin? " + fat.isVitamin());
            System.out.println("  Is mineral? " + fat.isMineral());
        }

        // Test carbohydrates (ID 205)
        NutrientV2 carbs = nutrientDAO.findById(205);
        if (carbs != null) {
            System.out.println("Found by ID 205: " + carbs.getDisplayNameWithUnit());
            System.out.println("  Is macronutrient? " + carbs.isMacronutrient());
            System.out.println("  Is vitamin? " + carbs.isVitamin());
            System.out.println("  Is mineral? " + carbs.isMineral());
        }

        // Count different types of nutrients using V2
        System.out.println("\n=== NUTRIENT CATEGORIES (V2) ===");
        int macroCount = 0, vitaminCount = 0, mineralCount = 0;
        for (NutrientV2 nutrient : nutrients) {
            if (nutrient.isMacronutrient()) macroCount++;
            if (nutrient.isVitamin()) vitaminCount++;
            if (nutrient.isMineral()) mineralCount++;
        }
        System.out.println("  Macronutrients: " + macroCount);
        System.out.println("  Vitamins: " + vitaminCount);
        System.out.println("  Minerals: " + mineralCount);

        System.out.println("\n=== SUCCESS: All V2 classes working with loaded data! ===");
        System.out.println("✅ Food Groups: " + totalGroups + " | Nutrients: " + totalNutrients);

        // Test NutrientAmountV2 and NutrientAmountDAOV2
        System.out.println("\n=== Testing NutrientAmountV2 ===");
        NutrientAmountDAOV2 nutrientAmountDAO = new NutrientAmountDAOV2();

        // Test our class works
        int totalNutrientAmounts = nutrientAmountDAO.count();
        System.out.println("Nutrient amounts in database: " + totalNutrientAmounts);

        // Show nutrition facts for fried egg (Food ID 129)
        System.out.println("\n=== NUTRITION FACTS FOR FRIED EGG (Food ID 129) ===");
        List<NutrientAmountV2> friedEggNutrition = nutrientAmountDAO.findAll().stream()
                .filter(na -> na.getFoodId() != null && na.getFoodId() == 129)
                .limit(10)  // Show first 10 nutrients
                .toList();

        for (NutrientAmountV2 na : friedEggNutrition) {
            NutrientV2 nutrient = nutrientDAO.findById(na.getNutrientId());
            if (nutrient != null) {
                System.out.printf("  • %s: %.2f %s\n",
                        nutrient.getNutrientName(),
                        na.getNutrientValue(),
                        nutrient.getNutrientUnit());
            }
        }

        // Test macronutrients for meal logging foods
        System.out.println("\n=== MACRONUTRIENTS FOR MEAL FOODS ===");
        int[] foodIds = {5, 129, 61, 2873, 1415}; // Chow mein, egg, milk, coffee, oats
        int[] macroNutrientIds = {203, 204, 205}; // Protein, Fat, Carbs

        for (int foodId : foodIds) {
            System.out.printf("\nFood ID %d:\n", foodId);
            for (int nutrientId : macroNutrientIds) {
                List<NutrientAmountV2> macroNutrients = nutrientAmountDAO.findAll().stream()
                        .filter(na -> na.getFoodId() != null && na.getFoodId() == foodId)
                        .filter(na -> na.getNutrientId() != null && na.getNutrientId() == nutrientId)
                        .toList();

                for (NutrientAmountV2 na : macroNutrients) {
                    NutrientV2 nutrient = nutrientDAO.findById(na.getNutrientId());
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
        NutrientV2 energyNutrient = nutrientDAO.findById(208); // Energy
        for (int foodId : foodIds) {
            List<NutrientAmountV2> calories = nutrientAmountDAO.findAll().stream()
                    .filter(na -> na.getFoodId() != null && na.getFoodId() == foodId)
                    .filter(na -> na.getNutrientId() != null && na.getNutrientId() == 208) // Energy
                    .toList();

            for (NutrientAmountV2 na : calories) {
                System.out.printf("  Food ID %d: %.0f %s\n",
                        foodId,
                        na.getNutrientValue(),
                        energyNutrient != null ? energyNutrient.getNutrientUnit() : "kcal");
            }
        }

        System.out.println("\n✅ NutrientAmountV2 Testing Complete! All V2 nutrition classes working!");

    }
}