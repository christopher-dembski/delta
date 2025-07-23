package statistics.services;

import statistics.models.NutrientStatistics;
import java.util.*;

public class MealStatisticsService implements IMealStatisticsService {
    @Override
    public NutrientStatistics calculateNutrientBreakdown(List<meals.models.meal.Meal> meals) {
        // TODO: Adapt this to use real Meal and MealItem data
        Map<String, Double> nutrientTotals = calculateNutrientTotalsFromMeals(meals);
        double totalWeight = nutrientTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<String, Double> nutrientPercentages = calculatePercentages(nutrientTotals, totalWeight);
        double totalCalories = nutrientTotals.getOrDefault("ENERGY (KILOCALORIES)", 0.0) * 1000;
        return new NutrientStatistics(nutrientPercentages, nutrientTotals, totalCalories, meals.size());
    }

    // New method for SampleMeal


    private Map<String, Double> calculateNutrientTotalsFromMeals(List<meals.models.meal.Meal> meals) {
        // TODO: Implement using real Meal/MealItem/NutrientAmount data
        Map<String, Double> allNutrientTotals = new HashMap<>();
        // Placeholder: implement aggregation logic here
        return allNutrientTotals;
    }

    private double convertToGrams(double value, String unit) {
        if (unit == null) return value;
        return switch (unit.toLowerCase()) {
            case "g" -> value;
            case "mg" -> value / 1000.0;
            case "µg", "ug", "mcg" -> value / 1000000.0;
            case "iu" -> value / 1000.0;
            case "re" -> value / 1000.0;
            case "dfe" -> value / 1000.0;
            case "nfe" -> value / 1000.0;
            case "te" -> value / 1000.0;
            case "kcal", "cal" -> value / 1000.0;
            case "kj" -> value / 4184.0;
            case "ne" -> value / 1000.0;
            default -> value;
        };
    }

    private boolean isWaterOrBulk(String nutrientName) {
        String name = nutrientName.toLowerCase();
        return name.contains("moisture") || 
               name.contains("ash") || 
               name.contains("alcohol") ||
               name.contains("caffeine") ||
               name.contains("theobromine");
    }

    private Map<String, Double> calculatePercentages(Map<String, Double> totals, double totalWeight) {
        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            double percentage = (entry.getValue() / totalWeight) * 100.0;
            percentages.put(entry.getKey(), percentage);
        }
        return percentages;
    }
} 
