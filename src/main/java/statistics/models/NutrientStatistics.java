package statistics.models;

import java.util.Map;

public class NutrientStatistics {
    private final Map<String, Double> nutrientPercentages;
    private final Map<String, Double> nutrientTotals;
    private final double totalCalories;
    private final int mealCount;

    public NutrientStatistics(Map<String, Double> nutrientPercentages, 
                            Map<String, Double> nutrientTotals, 
                            double totalCalories, 
                            int mealCount) {
        this.nutrientPercentages = nutrientPercentages;
        this.nutrientTotals = nutrientTotals;
        this.totalCalories = totalCalories;
        this.mealCount = mealCount;
    }

    public Map<String, Double> getNutrientPercentages() {
        return nutrientPercentages;
    }

    public Map<String, Double> getNutrientTotals() {
        return nutrientTotals;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public int getMealCount() {
        return mealCount;
    }
} 