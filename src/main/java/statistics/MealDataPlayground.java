package statistics;

import meals.services.QueryMealsService;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.models.food.Food;
import meals.models.food.Measure;
import meals.models.nutrient.Nutrient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Playground to demonstrate fetching meal data using the new services architecture
 * for statistics generation.
 */
public class MealDataPlayground {

    public static void main(String[] args) {
        System.out.println("🍽️ === MEAL DATA PLAYGROUND FOR STATISTICS ===");
        
        try {
            // Your test meal is on 2025-07-24, so let's query around that date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse("2025-07-24");
            Date endDate = sdf.parse("2025-07-24");
            
            System.out.println("📅 Querying meals from " + sdf.format(startDate) + " to " + sdf.format(endDate));
            
            // Fetch meals using the service
            System.out.println("🔍 Attempting to fetch meals...");
            QueryMealsService.QueryMealsServiceOutput result = QueryMealsService.instance().getMealsByDate(startDate, endDate);
            
            if (!result.ok()) {
                System.err.println("❌ Service returned errors: " + result.errors());
                return;
            }
            
            System.out.println("✅ Service call successful, checking meals...");
            
            List<Meal> meals = result.getMeals();
            System.out.println("📊 Found " + meals.size() + " meals");
            
            if (meals.isEmpty()) {
                System.out.println("⚠️  No meals found for the specified date range");
                return;
            }
            
            // Process each meal for statistics
            System.out.println("\n🔍 === MEAL BREAKDOWN FOR STATISTICS ===");
            
            for (int i = 0; i < meals.size(); i++) {
                Meal meal = meals.get(i);
                System.out.println("\n📋 Meal #" + (i + 1) + ":");
                System.out.println("   ID: " + meal.getId());
                System.out.println("   Type: " + meal.getMealType());
                System.out.println("   Date: " + meal.getCreatedAt());
                System.out.println("   Items: " + meal.getMealItems().size());
                
                // Process meal items
                List<MealItem> mealItems = meal.getMealItems();
                for (int j = 0; j < mealItems.size(); j++) {
                    MealItem item = mealItems.get(j);
                    System.out.println("\n   🍽️ Item #" + (j + 1) + ":");
                    System.out.println("      Item ID: " + item.getId());
                    System.out.println("      Quantity: " + item.getQuantity());
                    
                    // Process food information
                    Food food = item.getFood();
                    System.out.println("      Food: " + food.getFoodDescription());
                    System.out.println("      Food ID: " + food.getFoodId());
                    System.out.println("      Food Group: " + food.getFoodGroup().getFoodGroupName());
                    
                    // Process measure information
                    Measure measure = item.getSelectedMeasure();
                    System.out.println("      Measure: " + measure.getName());
                    System.out.println("      Conversion: " + measure.getConversionValue());
                    
                    // Process nutrients for statistics
                    Map<Nutrient, Float> nutrients = food.getNutrientAmounts();
                    System.out.println("      Nutrients available: " + nutrients.size());
                    
                    if (!nutrients.isEmpty()) {
                        System.out.println("      🧪 Top 5 Nutrients:");
                        nutrients.entrySet().stream()
                            .limit(5)
                            .forEach(entry -> {
                                Nutrient nutrient = entry.getKey();
                                Float amount = entry.getValue();
                                System.out.println("         " + nutrient.getNutrientName() + 
                                                 " (" + nutrient.getNutrientUnit() + "): " + amount);
                            });
                    }
                }
            }
            
            // Summary for statistics
            System.out.println("\n📊 === STATISTICS SUMMARY ===");
            int totalMealItems = meals.stream().mapToInt(meal -> meal.getMealItems().size()).sum();
            int totalNutrients = meals.stream()
                .flatMap(meal -> meal.getMealItems().stream())
                .mapToInt(item -> item.getFood().getNutrientAmounts().size())
                .sum();
            
            System.out.println("✅ Total meals: " + meals.size());
            System.out.println("✅ Total meal items: " + totalMealItems);
            System.out.println("✅ Total nutrient data points: " + totalNutrients);
            System.out.println("✅ Data ready for statistics generation! 📈");
            
        } catch (Exception e) {
            System.err.println("❌ Error in playground: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 