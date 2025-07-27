package statistics.presenter;

import meals.models.food.Food;
import meals.models.food.Measure;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.models.nutrient.Nutrient;
import meals.services.QueryFoodsService;
import meals.services.QueryMealsService;
import statistics.service.StatisticsService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Presenter for comparing nutrition between before and after swap meal lists using bar charts.
 */
public class SwapComparisonPresenter {
    
    /**
     * Creates a complete UI for swap comparison with demo data.
     */
    public JPanel createSwapComparisonUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Food Swap Comparison"));
        
        try {
            // Use the demo method for standalone usage
            JPanel chartPanel = presentDemoSwapComparison();
            mainPanel.add(chartPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading swap comparison: " + e.getMessage());
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
        }
        
        return mainPanel;
    }
    
    /**
     * Creates a swap comparison chart directly from two Food objects.
     * This is used for real swap workflow integration.
     * 
     * @param beforeFood The original food being replaced
     * @param afterFood The new food being swapped to
     * @param beforeLabel Label for the before food
     * @param afterLabel Label for the after food
     * @return JPanel containing the comparison chart
     */
    public JPanel createSwapComparisonFromFoods(Food beforeFood, Food afterFood, String beforeLabel, String afterLabel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Nutritional Comparison"));
        
        try {
            // Calculate nutrient totals for each food (using standard serving size)
            Map<String, Double> beforeNutrients = calculateNutrientTotalsFromFood(beforeFood);
            Map<String, Double> afterNutrients = calculateNutrientTotalsFromFood(afterFood);
            
            // Create the comparison chart
            JPanel chartPanel = createBarChartPanel(beforeNutrients, afterNutrients);
            mainPanel.add(chartPanel, BorderLayout.CENTER);
            
            // Add summary information
            JPanel summaryPanel = createFoodComparisonSummary(beforeFood, afterFood, beforeNutrients, afterNutrients);
            mainPanel.add(summaryPanel, BorderLayout.SOUTH);
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error creating food comparison: " + e.getMessage());
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
        }
        
        return mainPanel;
    }

    
    /**
     * Creates and returns a panel with bar chart comparing nutrition before/after swap.
     * 
     * @param beforeSwapMeals The meals before the swap
     * @param afterSwapMeals The meals after the swap
     * @return JPanel containing the comparison bar chart
     */
    public JPanel presentSwapComparison(List<Meal> beforeSwapMeals, List<Meal> afterSwapMeals) {
        try {
            // Calculate nutrition totals for both meal lists
            Map<String, Double> beforeTotals = StatisticsService.instance().calculateNutrientTotalsFromMeals(beforeSwapMeals);
            Map<String, Double> afterTotals = StatisticsService.instance().calculateNutrientTotalsFromMeals(afterSwapMeals);
            
            // Create bar chart comparing the totals
            return createBarChartPanel(beforeTotals, afterTotals);
            
        } catch (Exception e) {
            return createErrorPanel("Error calculating swap comparison: " + e.getMessage());
        }
    }
    
    /**
     * NEW: Enhanced version that prioritizes user's goal nutrients in the chart.
     * @param beforeSwapMeals The meals before the swap
     * @param afterSwapMeals The meals after the swap
     * @param goalNutrientNames List of nutrient names from user's goals (1-2 items)
     * @return JPanel containing the comparison bar chart with goal nutrients prioritized
     */
    public JPanel presentSwapComparison(List<Meal> beforeSwapMeals, List<Meal> afterSwapMeals, List<String> goalNutrientNames) {
        try {
            // Calculate nutrition totals for both meal lists
            Map<String, Double> beforeTotals = StatisticsService.instance().calculateNutrientTotalsFromMeals(beforeSwapMeals);
            Map<String, Double> afterTotals = StatisticsService.instance().calculateNutrientTotalsFromMeals(afterSwapMeals);
            
            // Create bar chart comparing the totals with goal nutrients prioritized
            return createBarChartPanelWithGoals(beforeTotals, afterTotals, goalNutrientNames);
            
        } catch (Exception e) {
            return createErrorPanel("Error calculating swap comparison: " + e.getMessage());
        }
    }
    
    private JPanel createBarChartPanel(Map<String, Double> before, Map<String, Double> after) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Add all nutrients from both before and after
        Set<String> allNutrients = new HashSet<>(before.keySet());
        allNutrients.addAll(after.keySet());
        
        for (String nutrient : allNutrients) {
            double beforeVal = before.getOrDefault(nutrient, 0.0);
            double afterVal = after.getOrDefault(nutrient, 0.0);
            dataset.addValue(beforeVal, "Before Swap", nutrient);
            dataset.addValue(afterVal, "After Swap", nutrient);
        }
        
        JFreeChart barChart = ChartFactory.createBarChart(
            "Nutrient Comparison: Before vs After Swap",
            "Nutrient",
            "Amount (g)",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        
        // Add summary label
        JLabel summaryLabel = createSummaryLabel(before, after);
        panel.add(summaryLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * NEW: Creates a bar chart panel with goal nutrients prioritized.
     */
    private JPanel createBarChartPanelWithGoals(Map<String, Double> before, Map<String, Double> after, List<String> goalNutrientNames) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Get prioritized nutrients (goals first, then top by amount)
        Map<String, Double> prioritizedBefore = getPrioritizedNutrientsForGoals(before, goalNutrientNames, 7);
        Map<String, Double> prioritizedAfter = getPrioritizedNutrientsForGoals(after, goalNutrientNames, 7);
        
        // Add all nutrients from both prioritized maps
        Set<String> allNutrients = new HashSet<>(prioritizedBefore.keySet());
        allNutrients.addAll(prioritizedAfter.keySet());
        
        // Add data to dataset in the order they appear in prioritizedBefore (goals first)
        for (String nutrient : prioritizedBefore.keySet()) {
            double beforeVal = prioritizedBefore.getOrDefault(nutrient, 0.0);
            double afterVal = prioritizedAfter.getOrDefault(nutrient, 0.0);
            dataset.addValue(beforeVal, "Before Swap", nutrient);
            dataset.addValue(afterVal, "After Swap", nutrient);
        }
        
        // Add any nutrients that are only in prioritizedAfter
        for (String nutrient : prioritizedAfter.keySet()) {
            if (!prioritizedBefore.containsKey(nutrient)) {
                double beforeVal = 0.0;
                double afterVal = prioritizedAfter.get(nutrient);
                dataset.addValue(beforeVal, "Before Swap", nutrient);
                dataset.addValue(afterVal, "After Swap", nutrient);
            }
        }
        
        JFreeChart barChart = ChartFactory.createBarChart(
            "Meal List Impact: Before vs After Swap (Goal Nutrients Prioritized)",
            "Nutrient",
            "Amount (g)",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        
        // Add summary label with goal highlighting
        JLabel summaryLabel = createGoalAwareSummaryLabel(before, after, goalNutrientNames);
        panel.add(summaryLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * NEW: Creates a summary label that highlights the impact on goal nutrients.
     */
    private JLabel createGoalAwareSummaryLabel(Map<String, Double> before, Map<String, Double> after, List<String> goalNutrientNames) {
        double beforeTotal = before.values().stream().mapToDouble(Double::doubleValue).sum();
        double afterTotal = after.values().stream().mapToDouble(Double::doubleValue).sum();
        double change = afterTotal - beforeTotal;
        
        StringBuilder html = new StringBuilder("<html><div style='text-align: center;'>");
        
        // Add goal nutrient specific changes
        if (!goalNutrientNames.isEmpty()) {
            html.append("<br/><b>Goal Nutrient Changes:</b><br/>");
            for (String goalNutrient : goalNutrientNames) {
                double beforeGoal = before.getOrDefault(goalNutrient, 0.0);
                double afterGoal = after.getOrDefault(goalNutrient, 0.0);
                double goalChange = afterGoal - beforeGoal;
                
                html.append(goalNutrient).append(": ");
                if (goalChange > 0) {
                    html.append("<font color='green'>+").append(String.format("%.2f", goalChange)).append("g</font>");
                } else if (goalChange < 0) {
                    html.append("<font color='red'>").append(String.format("%.2f", goalChange)).append("g</font>");
                } else {
                    html.append("<font color='gray'>No change</font>");
                }
                html.append("<br/>");
            }
        }
        
        html.append("</div></html>");
        
        JLabel label = new JLabel(html.toString());
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
    
    private JLabel createSummaryLabel(Map<String, Double> before, Map<String, Double> after) {
        double beforeTotal = before.values().stream().mapToDouble(Double::doubleValue).sum();
        double afterTotal = after.values().stream().mapToDouble(Double::doubleValue).sum();
        double change = afterTotal - beforeTotal;
        
        String changeText = change >= 0 ? 
            String.format("+%.1fg (+%.1f%%)", change, (change/beforeTotal)*100) :
            String.format("%.1fg (%.1f%%)", change, (change/beforeTotal)*100);
            
        String summaryText = String.format(
            "<html><div style='text-align: center; padding: 10px;'>" +
            "<b>Total Nutrition Change:</b> %s<br/>" +
            "Before: %.1fg | After: %.1fg" +
            "</div></html>",
            changeText, beforeTotal, afterTotal
        );
        
        JLabel label = new JLabel(summaryText, JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }
    
    private JPanel createErrorPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel errorLabel = new JLabel("<html><div style='text-align: center; color: red;'>" + 
                                       message + "</div></html>", JLabel.CENTER);
        panel.add(errorLabel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Demo method that creates sample data: 5 vs 10 * 100mL * chow mein comparison
     */
    public JPanel presentDemoSwapComparison() {
        try {
            // Create demo meal lists
            List<Meal> beforeMeals = createDemoBeforeMeals(); // 5 * 100mL * chow mein
            List<Meal> afterMeals = createDemoAfterMeals();   // 10 * 100mL * chow mein

            return presentSwapComparison(beforeMeals, afterMeals);

        } catch (Exception e) {
            return createErrorPanel("Failed to create demo swap comparison: " + e.getMessage());
        }
    }
    
    private List<Meal> createDemoBeforeMeals() throws Exception {
        // Use real data from database: 2025-07-25 to 2025-07-26
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2025-07-25");
        Date endDate = sdf.parse("2025-07-26");
        
        System.out.println("📅 [Demo Before] Fetching real meals from database: " + 
                         sdf.format(startDate) + " to " + sdf.format(endDate));
        
        // Query meals from the database
        QueryMealsService.QueryMealsServiceOutput result = 
            QueryMealsService.instance().getMealsByDate(startDate, endDate);
        
        if (!result.ok()) {
            System.err.println("❌ Failed to fetch meals: " + result.errors());
            throw new Exception("Failed to fetch meals for demo: " + result.errors());
        }
        
        List<Meal> meals = result.getMeals();
        System.out.println("✅ [Demo Before] Found " + meals.size() + " real meals from database");
        
        if (meals.isEmpty()) {
            System.out.println("⚠️  No meals found in database for the specified date range. Creating fallback demo data...");
            return createFallbackDemoMeals();
        }
        
        return meals;
    }
    
    private List<Meal> createFallbackDemoMeals() throws QueryFoodsService.QueryFoodsServiceException {
        // Fallback: Create demo data if no real meals exist
        System.out.println("🔄 Creating fallback demo meal with chow mein...");
        
        // Get chow mein from the database
        Food chowMein = QueryFoodsService.instance().findById(5); // "Chinese dish, chow mein, chicken"
        
        // Find 100mL measure or use first available
        Measure measure = chowMein.getPossibleMeasures().stream()
            .filter(m -> m.getName().toLowerCase().contains("ml") || 
                        m.getName().toLowerCase().contains("100"))
            .findFirst()
            .orElse(chowMein.getPossibleMeasures().get(0));
        
        // Create meal item: 5 * 100mL * chow mein
        MealItem chowMeinItem = new MealItem(1, chowMein, 5.0f, measure);
        
        // Create breakfast meal
        Meal breakfast = new Meal(
            1, 
            Meal.MealType.BREAKFAST, 
            List.of(chowMeinItem), 
            new Date(),
            1  // demo user ID
        );
        
        return List.of(breakfast);
    }
    
    private List<Meal> createDemoAfterMeals() throws QueryFoodsService.QueryFoodsServiceException {
        // Get same chow mein from the database
        Food chowMein = QueryFoodsService.instance().findById(5); // "Chinese dish, chow mein, chicken"
        
        // Find same measure
        Measure measure = chowMein.getPossibleMeasures().stream()
            .filter(m -> m.getName().toLowerCase().contains("ml") || 
                        m.getName().toLowerCase().contains("100"))
            .findFirst()
            .orElse(chowMein.getPossibleMeasures().get(0));
        
        // Create meal item: 10 * 100mL * chow mein (doubled quantity)
        MealItem chowMeinItem = new MealItem(2, chowMein, 10.0f, measure);
        
        // Create breakfast meal (after "swap" - really just increased quantity)
        Meal breakfast = new Meal(
            2, 
            Meal.MealType.BREAKFAST, 
            List.of(chowMeinItem), 
            new Date(),
            1  // demo user ID
        );
        
        return List.of(breakfast);
    }
    
    /**
     * Calculates nutrient totals from a single Food object using standard serving size.
     */
    private Map<String, Double> calculateNutrientTotalsFromFood(Food food) {
        Map<String, Double> nutrientTotals = new HashMap<>();
        
        // Use standard serving size (100g equivalent)
        float quantity = 1.0f;
        float conversionFactor = 1.0f; // Assume 100g serving
        
        Map<Nutrient, Float> nutrients = food.getNutrientAmounts();
        
        for (Map.Entry<Nutrient, Float> entry : nutrients.entrySet()) {
            Nutrient nutrient = entry.getKey();
            Float amount = entry.getValue();
            
            if (amount != null && amount > 0) {
                String nutrientName = nutrient.getNutrientName();
                String nutrientUnit = nutrient.getNutrientUnit();
                
                // Skip water and bulk nutrients
                if (StatisticsService.instance().isWaterOrBulk(nutrientName)) {
                    continue;
                }
                
                // Scale and convert to grams
                double scaledAmount = amount * conversionFactor * quantity;
                double amountInGrams = StatisticsService.instance().convertToGrams(scaledAmount, nutrientUnit);
                
                // Accumulate totals
                nutrientTotals.merge(nutrientName, amountInGrams, Double::sum);
            }
        }
        
        return getTopNutrientsWithOthers(nutrientTotals, 7);
    }
    
    /**
     * Takes the top N nutrients by amount and groups the rest as "Other nutrients".
     * Copied from StatisticsService.
     */
    private Map<String, Double> getTopNutrientsWithOthers(Map<String, Double> allNutrients, int topCount) {
        Map<String, Double> result = new HashMap<>();
        double otherSum = 0.0;
        
        List<Map.Entry<String, Double>> sortedEntries = allNutrients.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());
        
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            
            if (i < topCount) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                otherSum += entry.getValue();
            }
        }
        
        if (otherSum > 0) {
            result.put("Other nutrients", otherSum);
        }
        
        return result;
    }
    
    /**
     * NEW: Prioritizes user's goal nutrients first, then shows top nutrients by amount.
     * This makes the chart more relevant to what the user actually cares about.
     * 
     * @param allNutrients All available nutrients with their amounts
     * @param goalNutrientNames List of nutrient names from user's goals (1-2 items)
     * @param totalCount Total number of nutrients to show (including goals)
     * @return Map with goal nutrients first, then top nutrients, then "Others"
     */
    private Map<String, Double> getPrioritizedNutrientsForGoals(
            Map<String, Double> allNutrients, 
            List<String> goalNutrientNames, 
            int totalCount) {
        
        Map<String, Double> result = new LinkedHashMap<>(); // Preserve insertion order
        double otherSum = 0.0;
        Set<String> processedNutrients = new HashSet<>();
        
        System.out.println("🎯 Prioritizing goal nutrients: " + goalNutrientNames);
        
        // Step 1: Add goal nutrients first (if they exist in the data)
        for (String goalNutrient : goalNutrientNames) {
            if (allNutrients.containsKey(goalNutrient)) {
                result.put(goalNutrient, allNutrients.get(goalNutrient));
                processedNutrients.add(goalNutrient);
                System.out.println("🏆 Goal nutrient: " + goalNutrient + " = " + 
                                 String.format("%.3f", allNutrients.get(goalNutrient)) + "g");
            }
        }
        
        // Step 2: Fill remaining slots with top nutrients by amount (excluding goal nutrients)
        int remainingSlots = totalCount - result.size();
        List<Map.Entry<String, Double>> sortedEntries = allNutrients.entrySet().stream()
                .filter(entry -> !processedNutrients.contains(entry.getKey())) // Exclude already processed goal nutrients
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Sort by amount descending
                .collect(Collectors.toList());
        
        // Add top nutrients to fill remaining slots
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            
            if (i < remainingSlots) {
                result.put(entry.getKey(), entry.getValue());
                processedNutrients.add(entry.getKey());
                System.out.println("📊 Top nutrient: " + entry.getKey() + " = " + 
                                 String.format("%.3f", entry.getValue()) + "g");
            } else {
                otherSum += entry.getValue();
            }
        }
        
        // Step 3: Add "Other nutrients" if there are any remaining
        if (otherSum > 0) {
            result.put("Other nutrients", otherSum);
            System.out.println("📦 Other nutrients: " + String.format("%.3f", otherSum) + "g");
        }
        
        System.out.println("✅ Final chart will show " + result.size() + " categories");
        return result;
    }
    
    /**
     * Creates a summary panel for food comparison.
     */
    private JPanel createFoodComparisonSummary(Food beforeFood, Food afterFood, 
                                              Map<String, Double> beforeNutrients, 
                                              Map<String, Double> afterNutrients) {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Comparison Summary"));
        summaryPanel.setPreferredSize(new Dimension(800, 80));
        
        // Create before/after food info
        JPanel foodInfoPanel = new JPanel(new GridLayout(1, 2));
        
        JLabel beforeLabel = new JLabel("<html><b>Before:</b> " + beforeFood.getFoodDescription() + "</html>");
        beforeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel afterLabel = new JLabel("<html><b>After:</b> " + afterFood.getFoodDescription() + "</html>");
        afterLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        foodInfoPanel.add(beforeLabel);
        foodInfoPanel.add(afterLabel);
        
        summaryPanel.add(foodInfoPanel, BorderLayout.CENTER);
        
        return summaryPanel;
    }
} 