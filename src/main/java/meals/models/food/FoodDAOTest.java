package meals.models.food;

import data.DatabaseException;
import meals.models.nutrient.Nutrient;
import java.util.List;
import java.util.Map;

/**
 * Test class to demonstrate FoodDAO working with the existing Food.java class.
 * Shows how the DAO only fetches data needed for Food's specific fields.
 */
public class FoodDAOTest {
    
    public static void main(String[] args) throws DatabaseException {
        System.out.println("=== FoodDAO Test for Existing Food.java Class ===");
        
        FoodDAO foodDAO = new FoodDAO();
        
        // Test 1: Count foods
        int totalFoods = foodDAO.count();
        System.out.println("✅ Total foods in database: " + totalFoods);
        
        // Test 2: Get first few foods with all aggregated data
        System.out.println("\n=== First 3 Foods with Complete Data ===");
        List<Food> foods = foodDAO.findAll();
        
        for (int i = 0; i < Math.min(3, foods.size()); i++) {
            Food food = foods.get(i);
            
            System.out.printf("\n🍽️  Food %d: %s\n", food.getFoodId(), food.getFoodDescription());
            
            // Test FoodGroup aggregation
            FoodGroup group = food.getFoodGroup();
            if (group != null) {
                System.out.printf("   📂 Group: %s (ID: %d)\n", group.getFoodGroupName(), group.getFoodGroupId());
            } else {
                System.out.println("   📂 Group: None");
            }
            
            // Test nutrient amounts aggregation
            Map<Nutrient, Float> nutrients = food.getNutrientAmounts();
            System.out.printf("   🧪 Nutrients: %d loaded\n", nutrients.size());
            
            // Show first 3 nutrients
            int nutrientCount = 0;
            for (Map.Entry<Nutrient, Float> entry : nutrients.entrySet()) {
                if (nutrientCount >= 3) break;
                Nutrient nutrient = entry.getKey();
                Float value = entry.getValue();
                System.out.printf("     • %s: %.2f %s\n", 
                    nutrient.getNutrientName(), value, nutrient.getNutrientUnit());
                nutrientCount++;
            }
            
            // Test measures aggregation
            List<Measure> measures = food.getPossibleMeasures();
            System.out.printf("   📏 Measures: %d loaded\n", measures.size());
        }
        
        // Test 3: Find specific food by ID
        System.out.println("\n=== Test findById(5) - Chinese Chow Mein ===");
        Food chowMein = foodDAO.findById(5);
        if (chowMein != null) {
            System.out.printf("Found: %s\n", chowMein.getFoodDescription());
            System.out.printf("Group: %s\n", chowMein.getFoodGroup().getFoodGroupName());
            System.out.printf("Nutrients available: %d\n", chowMein.getNutrientAmounts().size());
            
            // Test the domain methods from Food class
            System.out.println("\n=== Testing Food Domain Methods ===");
            Map<Nutrient, Float> nutrients = chowMein.getNutrientAmounts();
            
            // Find protein content (if available)
            for (Map.Entry<Nutrient, Float> entry : nutrients.entrySet()) {
                if (entry.getKey().getNutrientName().toLowerCase().contains("protein")) {
                    Float proteinAmount = chowMein.getNutrientAmount(entry.getKey());
                    System.out.printf("Protein content: %.2f %s\n", 
                        proteinAmount, entry.getKey().getNutrientUnit());
                    break;
                }
            }
            
            // Show TOP 10 NUTRIENTS for Chow Mein from aggregated map
            System.out.println("\n=== TOP 10 NUTRIENTS FOR CHOW MEIN (From Aggregated Map) ===");
            nutrients.entrySet().stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue() > 0)  // Only non-zero values
                    .sorted((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()))     // Sort by value descending
                    .limit(10)  // Top 10
                    .forEach(entry -> {
                        Nutrient nutrient = entry.getKey();
                        Float value = entry.getValue();
                        System.out.printf("  🧪 %-25s: %8.2f %s\n", 
                            nutrient.getNutrientName(), value, nutrient.getNutrientUnit());
                    });
                    
            System.out.printf("\n📊 Total nutrients available for %s: %d\n", 
                chowMein.getFoodDescription(), nutrients.size());
        } else {
            System.out.println("Food ID 5 not found");
        }
        
        // Test 4: Search by description
        System.out.println("\n=== Test findByDescription('milk') ===");
        List<Food> milkFoods = foodDAO.findByDescription("milk");
        System.out.printf("Found %d foods containing 'milk':\n", milkFoods.size());
        
        for (int i = 0; i < Math.min(3, milkFoods.size()); i++) {
            Food food = milkFoods.get(i);
            System.out.printf("  • %s (ID: %d)\n", food.getFoodDescription(), food.getFoodId());
        }
        
        System.out.println("\n✅ FoodDAO Test Complete!");
        System.out.println("🎉 Successfully fetched only the data needed for existing Food.java class!");
    }
} 