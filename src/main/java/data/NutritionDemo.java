package data;

import java.util.List;

/**
 * Demo class showing how to use the nutrition classes and DAOs.
 * This provides a clean API for working with food composition data.
 */
public class NutritionDemo {
    
    public static void main(String[] args) throws DatabaseException {
        // Create DAO instances
        FoodGroupDAO foodGroupDAO = new FoodGroupDAO(new MySQLDriver(MySQLConfig.instance()));
        NutrientDAO nutrientDAO = new NutrientDAO(new MySQLDriver(MySQLConfig.instance()));
        FoodDAO foodDAO = new FoodDAO(new MySQLDriver(MySQLConfig.instance()), foodGroupDAO);
        NutrientAmountDAO nutrientAmountDAO = new NutrientAmountDAO(new MySQLDriver(MySQLConfig.instance()));
            
        System.out.println("=== Nutrition Demo ===");
        
        // 1. Find all food groups
        System.out.println("\n1. All food groups:");
        List<FoodGroup> allGroups = foodGroupDAO.findAll();
        for (int i = 0; i < Math.min(5, allGroups.size()); i++) {
            FoodGroup group = allGroups.get(i);
            System.out.println("  " + group.getDisplayName() + " (ID: " + group.getFoodGroupId() + ")");
        }
        
        // 2. Find all nutrients
        System.out.println("\n2. All nutrients (first 5):");
        List<Nutrient> allNutrients = nutrientDAO.findAll();
        for (int i = 0; i < Math.min(5, allNutrients.size()); i++) {
            Nutrient nutrient = allNutrients.get(i);
            System.out.println("  " + nutrient.getDisplayNameWithUnit());
        }
        
        // 3. Find all foods
        System.out.println("\n3. All foods (first 5):");
        List<Food> allFoods = foodDAO.findAll();
        for (int i = 0; i < Math.min(5, allFoods.size()); i++) {
            Food food = allFoods.get(i);
            System.out.println("  " + food.getDisplayName() + " (Code: " + food.getFoodCode() + ")");
        }
        
        // 4. Find food by ID
        System.out.println("\n4. Find food with ID 2:");
        Food food2 = foodDAO.findById(2);
        if (food2 != null) {
            System.out.println("  Found: " + food2);
        } else {
            System.out.println("  Food with ID 2 not found");
        }
        
        // 5. Count entities
        System.out.println("\n5. Counts:");
        System.out.println("  Total food groups: " + foodGroupDAO.count());
        System.out.println("  Total nutrients: " + nutrientDAO.count());
        System.out.println("  Total foods: " + foodDAO.count());
        
        System.out.println("\n=== Demo Complete ===");
    }
} 