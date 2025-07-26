// package statistics.presenter;

// import statistics.model.OldNutrientStatistics;
// import statistics.service.MealStatisticsService;

// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.ChartPanel;
// import org.jfree.chart.JFreeChart;
// import org.jfree.data.category.DefaultCategoryDataset;

// import javax.swing.*;
// import java.awt.*;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.HashMap;
// import data.NutrientAmountDAO;
// import data.NutrientDAO;
// import data.NutrientAmount;
// import shared.AppBackend;

// public class OldSwapComparisonPresenter {
//     private final MealStatisticsService statsService = new MealStatisticsService();

//     public JPanel presentSwapComparison() {
//         // Generate before and after swap sample meals
//         List<SampleMeal> beforeMeals = createSampleMeals(5);    // 12 meals of foodId 5
//         List<SampleMeal> afterMeals = createSampleMeals(5573);  // 12 meals of foodId 5573

//         Map<String, Double> beforeTotals = calculateNutrientTotalsFromMeals(beforeMeals);
//         Map<String, Double> afterTotals = calculateNutrientTotalsFromMeals(afterMeals);
//         double beforeWeight = beforeTotals.values().stream().mapToDouble(Double::doubleValue).sum();
//         double afterWeight = afterTotals.values().stream().mapToDouble(Double::doubleValue).sum();
//         Map<String, Double> beforePercentages = calculatePercentages(beforeTotals, beforeWeight);
//         Map<String, Double> afterPercentages = calculatePercentages(afterTotals, afterWeight);

//         // For the bar chart, use totals (not percentages)
//         return createBarChartPanel(beforeTotals, afterTotals);
//     }

//     // Copied from BaseStatisticsPresenter
//     private List<SampleMeal> createSampleMeals(int foodId) {
//         List<SampleMeal> sampleMeals = new ArrayList<>();
//         LocalDate date = LocalDate.of(2024, 1, 15);
//         for (int i = 0; i < 12; i++) {
//             sampleMeals.add(new SampleMeal(date, "lunch", foodId, 100.0));
//         }
//         return sampleMeals;
//     }

//     private Map<String, Double> calculateNutrientTotalsFromMeals(List<SampleMeal> sampleMeals) {
//         Map<String, Double> allNutrientTotals = new HashMap<>();
//         try {
//             // Fetch all nutrient data (mocked or real as needed)
//             NutrientAmountDAO nutrientAmountDAO = new NutrientAmountDAO(AppBackend.db());
//             NutrientDAO nutrientDAO = new NutrientDAO(AppBackend.db());
//             List<NutrientAmount> allNutrientAmounts = nutrientAmountDAO.findAll();
//             List<data.Nutrient> allNutrients = nutrientDAO.findAll();
//             Map<Integer, String> nutrientIdToName = new HashMap<>();
//             Map<Integer, String> nutrientIdToUnit = new HashMap<>();
//             for (data.Nutrient nutrient : allNutrients) {
//                 nutrientIdToName.put(nutrient.getNutrientId(), nutrient.getNutrientName());
//                 nutrientIdToUnit.put(nutrient.getNutrientId(), nutrient.getNutrientUnit());
//             }
//             for (SampleMeal meal : sampleMeals) {
//                 List<NutrientAmount> nutrientAmountsForFood = allNutrientAmounts.stream()
//                         .filter(na -> na.getFoodId().equals(meal.getFoodId()))
//                         .toList();
//                 for (NutrientAmount nutrientAmount : nutrientAmountsForFood) {
//                     String nutrientName = nutrientIdToName.get(nutrientAmount.getNutrientId());
//                     String nutrientUnit = nutrientIdToUnit.get(nutrientAmount.getNutrientId());
//                     if (nutrientName != null && nutrientAmount.getNutrientValue() != null) {
//                         if (isWaterOrBulk(nutrientName)) continue;
//                         double rawValue = nutrientAmount.getNutrientValue() * (meal.getQuantity() / 100.0);
//                         double valueInGrams = convertToGrams(rawValue, nutrientUnit);
//                         allNutrientTotals.merge(nutrientName, valueInGrams, Double::sum);
//                     }
//                 }
//             }
//             // Sort and group as in presenter
//             Map<String, Double> topNutrients = new HashMap<>();
//             double otherSum = 0.0;
//             List<Map.Entry<String, Double>> sortedEntries = allNutrientTotals.entrySet().stream()
//                     .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
//                     .toList();
//             for (int i = 0; i < sortedEntries.size(); i++) {
//                 Map.Entry<String, Double> entry = sortedEntries.get(i);
//                 if (i < 7) {
//                     topNutrients.put(entry.getKey(), entry.getValue());
//                 } else {
//                     otherSum += entry.getValue();
//                 }
//             }
//             if (otherSum > 0) {
//                 topNutrients.put("Other nutrients", otherSum);
//             }
//             return topNutrients;
//         } catch (Exception e) {
//             e.printStackTrace();
//             return allNutrientTotals;
//         }
//     }

//     private double convertToGrams(double value, String unit) {
//         if (unit == null) return value;
//         return switch (unit.toLowerCase()) {
//             case "g" -> value;
//             case "mg" -> value / 1000.0;
//             case "µg", "ug", "mcg" -> value / 1000000.0;
//             case "iu" -> value / 1000.0;
//             case "re" -> value / 1000.0;
//             case "dfe" -> value / 1000.0;
//             case "nfe" -> value / 1000.0;
//             case "te" -> value / 1000.0;
//             case "kcal", "cal" -> value / 1000.0;
//             case "kj" -> value / 4184.0;
//             case "ne" -> value / 1000.0;
//             default -> value;
//         };
//     }

//     private boolean isWaterOrBulk(String nutrientName) {
//         String name = nutrientName.toLowerCase();
//         return name.contains("moisture") || 
//                name.contains("ash") || 
//                name.contains("alcohol") ||
//                name.contains("caffeine") ||
//                name.contains("theobromine");
//     }

//     private Map<String, Double> calculatePercentages(Map<String, Double> totals, double totalWeight) {
//         Map<String, Double> percentages = new HashMap<>();
//         for (Map.Entry<String, Double> entry : totals.entrySet()) {
//             double percentage = (entry.getValue() / totalWeight) * 100.0;
//             percentages.put(entry.getKey(), percentage);
//         }
//         return percentages;
//     }

//     private JPanel createBarChartPanel(Map<String, Double> before, Map<String, Double> after) {
//         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//         for (String nutrient : before.keySet()) {
//             double beforeVal = before.getOrDefault(nutrient, 0.0);
//             double afterVal = after.getOrDefault(nutrient, 0.0);
//             dataset.addValue(beforeVal, "Before Swap", nutrient);
//             dataset.addValue(afterVal, "After Swap", nutrient);
//         }
//         for (String nutrient : after.keySet()) {
//             if (!before.containsKey(nutrient)) {
//                 double afterVal = after.getOrDefault(nutrient, 0.0);
//                 dataset.addValue(0.0, "Before Swap", nutrient);
//                 dataset.addValue(afterVal, "After Swap", nutrient);
//             }
//         }
//         JFreeChart barChart = ChartFactory.createBarChart(
//             "Nutrient Comparison Before/After Swap",
//             "Nutrient",
//             "Amount (g)",
//             dataset
//         );
//         ChartPanel chartPanel = new ChartPanel(barChart);
//         chartPanel.setPreferredSize(new Dimension(700, 500));
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.add(chartPanel, BorderLayout.CENTER);
//         return panel;
//     }

//     // Copied from BaseStatisticsPresenter
//     protected static class SampleMeal {
//         private final LocalDate date;
//         private final String mealType;
//         private final Integer foodId;
//         private final Double quantity;
//         public SampleMeal(LocalDate date, String mealType, Integer foodId, Double quantity) {
//             this.date = date;
//             this.mealType = mealType;
//             this.foodId = foodId;
//             this.quantity = quantity;
//         }
//         public LocalDate getDate() { return date; }
//         public String getMealType() { return mealType; }
//         public Integer getFoodId() { return foodId; }
//         public Double getQuantity() { return quantity; }
//         @Override
//         public String toString() {
//             return String.format("%s %s: Food %d (%.1fg)", date, mealType, foodId, quantity);
//         }
//     }
// } 