// package statistics.presenter;

// import meals.services.QueryMealsService;
// import meals.models.meal.Meal;
// import meals.models.meal.MealItem;
// import meals.models.food.Food;
// import meals.models.nutrient.Nutrient;
// import meals.models.food.Measure;

// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.ChartPanel;
// import org.jfree.chart.JFreeChart;
// import org.jfree.data.general.DefaultPieDataset;

// import javax.swing.*;
// import java.awt.*;
// import java.util.*;
// import java.util.Date;
// import java.util.List;
// import java.util.stream.Collectors;

// /**
//  * Presenter for creating nutrient breakdown visualizations from real meal data.
//  */
// public class NutrientBreakdownPresenter {

//     /**
//      * Creates a nutrient breakdown visualization from meal data for the specified date range.
//      * @param startDate The start date for meal data.
//      * @param endDate The end date for meal data.
//      * @return JPanel containing the pie chart and summary.
//      */
//     public JPanel presentNutrientBreakdown(Date startDate, Date endDate) {
//         try {
//             // Fetch meals using the service
//             QueryMealsService.QueryMealsServiceOutput result = 
//                 QueryMealsService.instance().getMealsByDate(startDate, endDate);
            
//             if (!result.ok()) {
//                 return createErrorPanel("Failed to fetch meals: " + result.errors());
//             }
            
//             List<Meal> meals = result.getMeals();
            
//             if (meals.isEmpty()) {
//                 return createNoDataPanel("No meals found for the specified date range.");
//             }
            
//             // Calculate nutrient totals from real meal data
//             Map<String, Double> nutrientTotals = calculateNutrientTotalsFromMeals(meals);
            
//             if (nutrientTotals.isEmpty()) {
//                 return createNoDataPanel("No nutrient data available for the selected meals.");
//             }
            
//             // Create visualization
//             return createVisualization(nutrientTotals, meals.size());
            
//         } catch (Exception e) {
//             e.printStackTrace();
//             return createErrorPanel("Error generating nutrient breakdown: " + e.getMessage());
//         }
//     }
    
//     /**
//      * Calculates total nutrients from all meals, converting units to grams for comparison.
//      */
//     private Map<String, Double> calculateNutrientTotalsFromMeals(List<Meal> meals) {
//         Map<String, Double> allNutrientTotals = new HashMap<>();
        
//         System.out.println("🧮 Calculating nutrient totals from " + meals.size() + " meals...");
        
//         for (Meal meal : meals) {
//             System.out.println("🔍 Processing meal: " + meal.getMealType() + " on " + meal.getCreatedAt());
            
//             for (MealItem mealItem : meal.getMealItems()) {
//                 Food food = mealItem.getFood();
//                 Float quantity = mealItem.getQuantity();
//                 Measure measure = mealItem.getSelectedMeasure();
//                 Float conversionFactor = measure.getConversionValue();
                
//                 System.out.println("   FOOD: Processing food: " + food.getFoodDescription());
//                 System.out.println("        Quantity: " + quantity + " x " + measure.getName());
//                 System.out.println("        Conversion factor: " + conversionFactor);
                
//                 Map<Nutrient, Float> nutrients = food.getNutrientAmounts();
//                 System.out.println("        Found " + nutrients.size() + " nutrients for this food");
                
//                 for (Map.Entry<Nutrient, Float> entry : nutrients.entrySet()) {
//                     Nutrient nutrient = entry.getKey();
//                     Float amount = entry.getValue();
                    
//                     if (amount != null && amount > 0) {
//                         String nutrientName = nutrient.getNutrientName();
//                         String nutrientUnit = nutrient.getNutrientUnit();
                        
//                         System.out.println("      DEBUG: Processing nutrient '" + nutrientName + "' (" + nutrientUnit + ")");
                        
//                         // Skip water and bulk nutrients that would skew visualization
//                         if (isWaterOrBulk(nutrientName)) {
//                             continue;
//                         }
                        
//                         // Correct scaling: base_amount × conversion_factor × quantity
//                         double scaledAmount = amount * conversionFactor * quantity;
                        
//                         // Convert to grams for consistent comparison
//                         double amountInGrams = convertToGrams(scaledAmount, nutrientUnit);
                        
//                         // Debug for carbohydrate specifically
//                         if (nutrientName.toUpperCase().contains("CARBOHYDRATE")) {
//                             System.out.println("      >>> CARB DEBUG: '" + nutrientName + "'");
//                             System.out.println("          Base amount: " + amount + " " + nutrientUnit);
//                             System.out.println("          Conversion factor: " + conversionFactor);
//                             System.out.println("          Quantity: " + quantity);
//                             System.out.println("          Scaled amount: " + scaledAmount + " " + nutrientUnit + " (= " + amount + " × " + conversionFactor + " × " + quantity + ")");
//                             System.out.println("          Converted to grams: " + amountInGrams + "g");
//                         }
                        
//                         // Accumulate totals
//                         System.out.println("      DEBUG: Adding nutrient '" + nutrientName + "' = " + amountInGrams + "g");
//                         allNutrientTotals.merge(nutrientName, amountInGrams, Double::sum);
//                     }
//                 }
//             }
//         }
        
//         System.out.println("TOTAL: Total unique nutrients collected: " + allNutrientTotals.size());
        
//         // DEBUG: Show what's actually in the map
//         System.out.println("DEBUG: Final nutrient map contents:");
//         for (Map.Entry<String, Double> entry : allNutrientTotals.entrySet()) {
//             System.out.println("  '" + entry.getKey() + "' = " + entry.getValue() + "g");
//         }
        
//         // Get top 7 nutrients and group the rest
//         return getTopNutrientsWithOthers(allNutrientTotals, 7);
//     }
    
//     /**
//      * Takes the top N nutrients by amount and groups the rest as "Other nutrients".
//      */
//     private Map<String, Double> getTopNutrientsWithOthers(Map<String, Double> allNutrients, int topCount) {
//         Map<String, Double> result = new HashMap<>();
//         double otherSum = 0.0;
        
//         List<Map.Entry<String, Double>> sortedEntries = allNutrients.entrySet().stream()
//                 .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
//                 .collect(Collectors.toList());
        
//         for (int i = 0; i < sortedEntries.size(); i++) {
//             Map.Entry<String, Double> entry = sortedEntries.get(i);
            
//             if (i < topCount) {
//                 result.put(entry.getKey(), entry.getValue());
//                 System.out.println("🥇 Top " + (i+1) + ": " + entry.getKey() + " = " + 
//                                  String.format("%.3f", entry.getValue()) + "g");
//             } else {
//                 otherSum += entry.getValue();
//             }
//         }
        
//         if (otherSum > 0) {
//             result.put("Other nutrients", otherSum);
//             System.out.println("📦 Other nutrients: " + String.format("%.3f", otherSum) + "g");
//         }
        
//         return result;
//     }
    
//     /**
//      * Converts nutrient amounts to grams for consistent comparison.
//      */
//     private double convertToGrams(double value, String unit) {
//         if (unit == null) return value;
        
//         return switch (unit.toLowerCase()) {
//             case "g" -> value;                          // Already in grams
//             case "mg" -> value / 1000.0;               // Milligrams to grams
//             case "µg", "ug", "mcg" -> value / 1000000.0; // Micrograms to grams
//             case "iu" -> value / 1000.0;               // International Units (rough conversion)
//             case "re" -> value / 1000.0;               // Retinol Equivalents (rough conversion)
//             case "dfe" -> value / 1000.0;              // Dietary Folate Equivalents (rough conversion)
//             case "nfe" -> value / 1000.0;              // Niacin Equivalents (rough conversion)
//             case "te" -> value / 1000.0;               // Tocopherol Equivalents (rough conversion)
//             case "kcal", "cal" -> value / 1000.0;      // Calories (treat as grams for visualization)
//             case "kj" -> value / 4184.0;               // Kilojoules to grams (rough energy conversion)
//             case "ne" -> value / 1000.0;               // Niacin Equivalent (mg to g)
//             default -> {
//                 System.out.println("⚠️  Unknown unit '" + unit + "', treating as grams");
//                 yield value;
//             }
//         };
//     }
    
//     /**
//      * Filters out water and bulk nutrients that would dominate the visualization.
//      */
//     private boolean isWaterOrBulk(String nutrientName) {
//         String name = nutrientName.toLowerCase();
//         return name.contains("moisture") || 
//                name.contains("ash") || 
//                name.contains("alcohol") ||
//                name.contains("caffeine") ||
//                name.contains("theobromine");
//     }
    
//     /**
//      * Creates the main visualization panel with pie chart and summary.
//      */
//     private JPanel createVisualization(Map<String, Double> nutrientTotals, int mealCount) {
//         JPanel mainPanel = new JPanel(new BorderLayout());
//         mainPanel.setBorder(BorderFactory.createTitledBorder("Nutrient Breakdown for Selected Meals"));
        
//         // Calculate percentages for pie chart
//         double totalWeight = nutrientTotals.values().stream().mapToDouble(Double::doubleValue).sum();
//         Map<String, Double> percentages = new HashMap<>();
//         for (Map.Entry<String, Double> entry : nutrientTotals.entrySet()) {
//             double percentage = (entry.getValue() / totalWeight) * 100.0;
//             percentages.put(entry.getKey(), percentage);
//         }
        
//         // Create pie chart
//         DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
//         for (Map.Entry<String, Double> entry : percentages.entrySet()) {
//             dataset.setValue(entry.getKey(), entry.getValue());
//         }
        
//         JFreeChart chart = ChartFactory.createPieChart(
//                 "Nutrient Distribution by Weight",
//                 dataset,
//                 true, true, false
//         );
        
//         ChartPanel chartPanel = new ChartPanel(chart);
//         chartPanel.setPreferredSize(new Dimension(600, 500));
        
//         mainPanel.add(chartPanel, BorderLayout.CENTER);
//         mainPanel.add(createSummaryPanel(nutrientTotals, percentages, mealCount), BorderLayout.SOUTH);
        
//         return mainPanel;
//     }
    
//     /**
//      * Creates a summary panel showing top nutrients and meal information.
//      */
//     private JPanel createSummaryPanel(Map<String, Double> totals, Map<String, Double> percentages, int mealCount) {
//         JPanel summaryPanel = new JPanel();
//         summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.X_AXIS));
//         summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
//         summaryPanel.setPreferredSize(new Dimension(600, 90));
        
//         // Show top 4 nutrients by weight
//         List<Map.Entry<String, Double>> topNutrients = totals.entrySet().stream()
//                 .filter(entry -> !entry.getKey().equals("Other nutrients"))
//                 .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
//                 .limit(4)
//                 .collect(Collectors.toList());
        
//         for (Map.Entry<String, Double> entry : topNutrients) {
//             String nutrientName = entry.getKey();
//             double percentage = percentages.get(nutrientName);
//             double weight = entry.getValue();
            
//             JLabel label = new JLabel(String.format(
//                     "<html><b>%s</b><br/>%.1f%% (%.3fg)</html>", 
//                     nutrientName, percentage, weight
//             ));
//             label.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
//             summaryPanel.add(label);
//         }
        
//         summaryPanel.add(Box.createHorizontalStrut(20));
//         JLabel mealCountLabel = new JLabel("Total Meals: " + mealCount);
//         mealCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
//         summaryPanel.add(mealCountLabel);
        
//         return summaryPanel;
//     }
    
//     /**
//      * Creates an error panel when something goes wrong.
//      */
//     private JPanel createErrorPanel(String errorMessage) {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBorder(BorderFactory.createTitledBorder("Error"));
        
//         JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
//                 "<h2>❌ Error</h2>" +
//                 "<p>" + errorMessage + "</p>" +
//                 "</div></html>");
//         errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
//         panel.add(errorLabel, BorderLayout.CENTER);
        
//         return panel;
//     }
    
//     /**
//      * Creates a panel when no data is available.
//      */
//     private JPanel createNoDataPanel(String message) {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBorder(BorderFactory.createTitledBorder("No Data"));
        
//         JLabel noDataLabel = new JLabel("<html><div style='text-align: center;'>" +
//                 "<h2>📊 No Data Available</h2>" +
//                 "<p>" + message + "</p>" +
//                 "<p>Please select a different date range or add some meal data.</p>" +
//                 "</div></html>");
//         noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
//         panel.add(noDataLabel, BorderLayout.CENTER);
        
//         return panel;
//     }
// } 