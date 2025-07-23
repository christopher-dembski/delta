package meals.services;

import data.FoodDAO;
import data.MySQLConfig;
import data.MySQLDriver;
import data.Food;
import data.DatabaseException;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.models.meal.Meal.MealType;
import java.text.SimpleDateFormat;
import java.util.*;

public class SeedMealsDemo {
    public static void main(String[] args) {
        // Dates for 3 days
        String[] dateStrings = {"2024-01-15", "2024-01-16", "2024-01-17"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int totalInserted = 0;
        List<Food> realFoods = getRealFoods();
        if (realFoods.isEmpty()) {
            System.err.println("No real foods found in the database. Please import foods first.");
            return;
        }
        for (String dateStr : dateStrings) {
            try {
                Date date = sdf.parse(dateStr);
                // Insert 1 breakfast
                insertMealForDay(MealType.BREAKFAST, date, realFoods);
                // Insert 1 lunch
                insertMealForDay(MealType.LUNCH, date, realFoods);
                // Insert 1 dinner
                insertMealForDay(MealType.DINNER, date, realFoods);
                // Insert 1 snack
                insertMealForDay(MealType.SNACK, date, realFoods);
                // Optionally, insert a second snack for variety
                if (dateStr.equals("2024-01-16")) {
                    insertMealForDay(MealType.SNACK, date, realFoods);
                }
                totalInserted += 4;
            } catch (Exception e) {
                System.err.println("Failed to insert meals for date " + dateStr + ": " + e.getMessage());
            }
        }
        System.out.println("✅ Seeded demo meals with real foods. Run your app to see them in statistics!");
    }

    private static List<Food> getRealFoods() {
        try {
            FoodDAO foodDAO = new FoodDAO(new MySQLDriver(MySQLConfig.instance()), null);
            return foodDAO.findAll();
        } catch (DatabaseException e) {
            System.err.println("Error fetching foods from DB: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static void insertMealForDay(MealType mealType, Date date, List<Food> realFoods) {
        int mealId = new Random().nextInt(1000000);
        List<MealItem> items = new ArrayList<>();
        Random rand = new Random();
        // Add 2 real foods per meal for variety
        for (int i = 0; i < 2; i++) {
            Food food = realFoods.get(rand.nextInt(realFoods.size()));
            // Create a minimal MealItem using only the food ID and a default quantity
            // Use null or a dummy value for Measure if required by the constructor
            try {
                // Try to use the MealItem constructor that takes only foodId if available
                // Otherwise, use a minimal Food object with the correct ID
                meals.models.food.Food dummyFood = new meals.models.food.Food(food.getFoodId(), "Seeded Food", null, null, null);
                meals.models.food.Measure dummyMeasure = new meals.models.food.Measure(1, "100g", 100.0f);
                MealItem mealItem = new MealItem(rand.nextInt(1000000), dummyFood, 1.0f, dummyMeasure);
                items.add(mealItem);
            } catch (Exception e) {
                System.err.println("Failed to create MealItem for food_id=" + food.getFoodId() + ": " + e.getMessage());
            }
        }
        if (items.isEmpty()) {
            System.err.println("No valid meal items for " + mealType + " on " + date + ", skipping meal.");
            return;
        }
        Meal meal = new Meal(mealId, mealType, items, date);
        CreateMealService.CreateMealServiceOutput result = CreateMealService.instance().createMeal(meal);
        if (result.ok()) {
            System.out.println("Inserted " + mealType + " for " + date);
        } else {
            System.out.println("❌ Failed to insert " + mealType + " for " + date + ": " + result.errors().getFirst());
        }
    }
} 