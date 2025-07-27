package statistics.model;

import java.util.Map;

/**
 * Domain model representing nutrient breakdown analysis results.
 * Contains all data needed for visualization.
 */
public class NutrientSummary {
    
    private final Map<String, Double> nutrientTotals;
    private final Map<String, Double> nutrientPercentages;
    private final int totalMeals;
    private final double totalWeight;
    
    public NutrientSummary(Map<String, Double> nutrientTotals, 
                          Map<String, Double> nutrientPercentages,
                          int totalMeals,
                          double totalWeight) {
        this.nutrientTotals = nutrientTotals;
        this.nutrientPercentages = nutrientPercentages;
        this.totalMeals = totalMeals;
        this.totalWeight = totalWeight;
    }
    
    public Map<String, Double> getNutrientTotals() {
        return nutrientTotals;
    }
    
    public Map<String, Double> getNutrientPercentages() {
        return nutrientPercentages;
    }
    
    public int getTotalMeals() {
        return totalMeals;
    }
    
    public double getTotalWeight() {
        return totalWeight;
    }
    
    public boolean isEmpty() {
        return nutrientTotals.isEmpty();
    }
} 