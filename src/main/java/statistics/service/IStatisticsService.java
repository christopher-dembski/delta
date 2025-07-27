package statistics.service;

import statistics.model.NutrientSummary;
import meals.models.meal.Meal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service interface for statistics calculations and data processing.
 * Follows the profile module pattern for clean separation of concerns.
 */
public interface IStatisticsService {
    
    /**
     * Calculate nutrient breakdown summary from a list of meals.
     * 
     * @param meals List of meals to analyze
     * @return NutrientSummary containing totals, percentages, and metadata
     */
    NutrientSummary calculateNutrientBreakdown(List<Meal> meals);
    
    /**
     * Fetch meals for a given date range.
     * 
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return List of meals in the date range
     * @throws StatisticsException if data retrieval fails
     */
    List<Meal> getMealsByDateRange(Date startDate, Date endDate) throws StatisticsException;
    
    /**
     * Convert nutrient amounts to grams for consistent comparison.
     * 
     * @param value The numeric value
     * @param unit The unit string (g, mg, µg, etc.)
     * @return Value converted to grams
     */
    double convertToGrams(double value, String unit);
    
    /**
     * Check if a nutrient is considered bulk/water content and should be filtered.
     * 
     * @param nutrientName The name of the nutrient
     * @return true if it's a bulk nutrient that should be excluded from main visualization
     */
    boolean isWaterOrBulk(String nutrientName);
    
    /**
     * Calculate raw nutrient totals from meals.
     * 
     * @param meals List of meals to analyze
     * @return Map of nutrient name to total amount in grams
     */
    Map<String, Double> calculateNutrientTotalsFromMeals(List<Meal> meals);
    
    /**
     * Exception thrown when statistics operations fail.
     */
    class StatisticsException extends Exception {
        public StatisticsException(String message) {
            super(message);
        }
        
        public StatisticsException(String message, Throwable cause) {
            super(message, cause);
        }
    }
} 