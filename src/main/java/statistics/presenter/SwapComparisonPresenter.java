package statistics.presenter;

import meals.models.food.Food;
import meals.models.food.Measure;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.models.nutrient.Nutrient;
import meals.services.QueryFoodsService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Presenter for comparing nutrition between before and after swap meal lists using bar charts.
 */
public class SwapComparisonPresenter {
    
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
            Map<String, Double> beforeTotals = calculateNutrientTotalsFromMeals(beforeSwapMeals);
            Map<String, Double> afterTotals = calculateNutrientTotalsFromMeals(afterSwapMeals);
            
            // Create bar chart comparing the totals
            return createBarChartPanel(beforeTotals, afterTotals);
            
        } catch (Exception e) {
            return createErrorPanel("Error calculating swap comparison: " + e.getMessage());
        }
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
    
    /**
     * Calculates total nutrients from all meals, using the same logic as NutrientBreakdownPresenter.
     */
    private Map<String, Double> calculateNutrientTotalsFromMeals(List<Meal> meals) {
        Map<String, Double> allNutrientTotals = new HashMap<>();
        
        System.out.println("🧮 [SwapComparison] Calculating nutrient totals from " + meals.size() + " meals...");
        
        for (Meal meal : meals) {
            System.out.println("🔍 Processing meal: " + meal.getMealType() + " on " + meal.getCreatedAt());
            
            for (MealItem mealItem : meal.getMealItems()) {
                Food food = mealItem.getFood();
                Float quantity = mealItem.getQuantity();
                Measure measure = mealItem.getSelectedMeasure();
                Float conversionFactor = measure.getConversionValue();
                
                System.out.println("   FOOD: Processing food: " + food.getFoodDescription());
                System.out.println("        Quantity: " + quantity + " x " + measure.getName());
                System.out.println("        Conversion factor: " + conversionFactor);
                
                Map<Nutrient, Float> nutrients = food.getNutrientAmounts();
                System.out.println("        Found " + nutrients.size() + " nutrients for this food");
                
                for (Map.Entry<Nutrient, Float> entry : nutrients.entrySet()) {
                    Nutrient nutrient = entry.getKey();
                    Float amount = entry.getValue();
                    
                    if (amount != null && amount > 0) {
                        String nutrientName = nutrient.getNutrientName();
                        String nutrientUnit = nutrient.getNutrientUnit();
                        
                        // Skip water and bulk nutrients that would skew visualization
                        if (isWaterOrBulk(nutrientName)) {
                            continue;
                        }
                        
                        // Correct scaling: base_amount × conversion_factor × quantity
                        double scaledAmount = amount * conversionFactor * quantity;
                        
                        // Convert to grams for consistent comparison
                        double amountInGrams = convertToGrams(scaledAmount, nutrientUnit);
                        
                        // Debug for carbohydrate specifically
                        if (nutrientName.toUpperCase().contains("CARBOHYDRATE")) {
                            System.out.println("      >>> CARB DEBUG: '" + nutrientName + "'");
                            System.out.println("          Base amount: " + amount + " " + nutrientUnit);
                            System.out.println("          Conversion factor: " + conversionFactor);
                            System.out.println("          Quantity: " + quantity);
                            System.out.println("          Scaled amount: " + scaledAmount + " " + nutrientUnit + " (= " + amount + " × " + conversionFactor + " × " + quantity + ")");
                            System.out.println("          Converted to grams: " + amountInGrams + "g");
                        }
                        
                        // Accumulate totals
                        allNutrientTotals.merge(nutrientName, amountInGrams, Double::sum);
                    }
                }
            }
        }
        
        System.out.println("TOTAL: Total unique nutrients collected: " + allNutrientTotals.size());
        
        // Get top 7 nutrients and group the rest (same as NutrientBreakdownPresenter)
        return getTopNutrientsWithOthers(allNutrientTotals, 7);
    }
    
    /**
     * Takes the top N nutrients by amount and groups the rest as "Other nutrients".
     * Copied from NutrientBreakdownPresenter.
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
                System.out.println("🥇 Top " + (i+1) + ": " + entry.getKey() + " = " + 
                                 String.format("%.3f", entry.getValue()) + "g");
            } else {
                otherSum += entry.getValue();
            }
        }
        
        if (otherSum > 0) {
            result.put("Other nutrients", otherSum);
            System.out.println("📦 Other nutrients: " + String.format("%.3f", otherSum) + "g");
        }
        
        return result;
    }
    
    /**
     * Converts nutrient amounts to grams for consistent comparison.
     * Copied from NutrientBreakdownPresenter.
     */
    private double convertToGrams(double value, String unit) {
        if (unit == null) return value;
        
        return switch (unit.toLowerCase()) {
            case "g" -> value;                          // Already in grams
            case "mg" -> value / 1000.0;               // Milligrams to grams
            case "µg", "ug", "mcg" -> value / 1000000.0; // Micrograms to grams
            case "iu" -> value / 1000.0;               // International Units (rough conversion)
            case "re" -> value / 1000.0;               // Retinol Equivalents (rough conversion)
            case "dfe" -> value / 1000.0;              // Dietary Folate Equivalents (rough conversion)
            case "nfe" -> value / 1000.0;              // Niacin Equivalents (rough conversion)
            case "te" -> value / 1000.0;               // Tocopherol Equivalents (rough conversion)
            case "kcal", "cal" -> value / 1000.0;      // Calories (treat as grams for visualization)
            case "kj" -> value / 4184.0;               // Kilojoules to grams (rough energy conversion)
            case "ne" -> value / 1000.0;               // Niacin Equivalent (mg to g)
            default -> {
                System.out.println("⚠️  Unknown unit '" + unit + "', treating as grams");
                yield value;
            }
        };
    }
    
    /**
     * Filters out water and bulk nutrients that would dominate the visualization.
     * Copied from NutrientBreakdownPresenter.
     */
    private boolean isWaterOrBulk(String nutrientName) {
        String name = nutrientName.toLowerCase();
        return name.contains("moisture") || 
               name.contains("ash") || 
               name.contains("alcohol") ||
               name.contains("caffeine") ||
               name.contains("theobromine");
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
    
    private List<Meal> createDemoBeforeMeals() throws QueryFoodsService.QueryFoodsServiceException {
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
            new Date()
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
            new Date()
        );
        
        return List.of(breakfast);
    }
    
    /**
     * Main method for testing the swap comparison presenter
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swap Comparison Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            SwapComparisonPresenter presenter = new SwapComparisonPresenter();
            JPanel chartPanel = presenter.presentDemoSwapComparison();
            
            frame.add(chartPanel);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
} 