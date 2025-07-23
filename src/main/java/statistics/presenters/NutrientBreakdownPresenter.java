package statistics.presenters;

import data.*;
import statistics.models.NutrientStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutrientBreakdownPresenter extends BaseStatisticsPresenter {

    public NutrientBreakdownPresenter() {
        super();
    }

    @Override
    protected NutrientStatistics calculateStatistics(List<SampleMeal> sampleMeals) throws Exception {
        Map<String, Double> nutrientTotals = calculateNutrientTotalsFromMeals(sampleMeals);
        
        // Calculate total weight of all nutrients (in grams)
        double totalWeight = nutrientTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("DEBUG: Total weight of all nutrients: " + totalWeight + "g");
        
        Map<String, Double> nutrientPercentages = calculatePercentages(nutrientTotals, totalWeight);
        double totalCalories = nutrientTotals.getOrDefault("ENERGY (KILOCALORIES)", 0.0) * 1000; // Convert back to kcal
        
        return new NutrientStatistics(nutrientPercentages, nutrientTotals, totalCalories, sampleMeals.size());
    }

    @Override
    protected JPanel createVisualization(Object statisticsData) {
        NutrientStatistics stats = (NutrientStatistics) statisticsData;
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Daily Nutrient Breakdown"));
        
        // Create pie chart
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Double> entry : stats.getNutrientPercentages().entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Nutrient Distribution by Weight",
                dataset,
                true, true, false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 500)); // Larger chart
        
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(stats), BorderLayout.SOUTH); // Move summary to bottom
        return mainPanel;
    }

    private Map<String, Double> calculateNutrientTotalsFromMeals(List<SampleMeal> sampleMeals) throws Exception {
        Map<String, Double> allNutrientTotals = new HashMap<>();
        
        List<NutrientAmount> allNutrientAmounts = fetchAllNutrientAmounts();
        List<Nutrient> allNutrients = fetchAllNutrients();
        
        Map<Integer, String> nutrientIdToName = new HashMap<>();
        Map<Integer, String> nutrientIdToUnit = new HashMap<>();
        for (Nutrient nutrient : allNutrients) {
            nutrientIdToName.put(nutrient.getNutrientId(), nutrient.getNutrientName());
            nutrientIdToUnit.put(nutrient.getNutrientId(), nutrient.getNutrientUnit());
        }
        
        for (SampleMeal meal : sampleMeals) {
            System.out.println("DEBUG: Processing meal: " + meal);
            
            List<NutrientAmount> nutrientAmountsForFood = allNutrientAmounts.stream()
                    .filter(na -> na.getFoodId().equals(meal.getFoodId()))
                    .toList();
            
            System.out.println("DEBUG: Found " + nutrientAmountsForFood.size() + " nutrients for food " + meal.getFoodId());
            
            for (NutrientAmount nutrientAmount : nutrientAmountsForFood) {
                String nutrientName = nutrientIdToName.get(nutrientAmount.getNutrientId());
                String nutrientUnit = nutrientIdToUnit.get(nutrientAmount.getNutrientId());
                
                if (nutrientName != null && nutrientAmount.getNutrientValue() != null) {
                    // Filter out water and bulk nutrients that skew the visualization
                    if (isWaterOrBulk(nutrientName)) {
                        continue;
                    }
                    
                    double rawValue = nutrientAmount.getNutrientValue() * (meal.getQuantity() / 100.0);
                    double valueInGrams = convertToGrams(rawValue, nutrientUnit);
                    
                    allNutrientTotals.merge(nutrientName, valueInGrams, Double::sum);
                }
            }
        }
        
        System.out.println("DEBUG: Total unique nutrients collected: " + allNutrientTotals.size());
        
        // Sort by value (descending) and take top 7, group rest as "Other"
        Map<String, Double> topNutrients = new HashMap<>();
        double otherSum = 0.0;
        
        List<Map.Entry<String, Double>> sortedEntries = allNutrientTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .toList();
        
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, Double> entry = sortedEntries.get(i);
            if (i < 7) {
                topNutrients.put(entry.getKey(), entry.getValue());
                System.out.println("DEBUG: Top nutrient " + (i+1) + ": " + entry.getKey() + " = " + entry.getValue() + "g");
            } else {
                otherSum += entry.getValue();
            }
        }
        
        if (otherSum > 0) {
            topNutrients.put("Other nutrients", otherSum);
            System.out.println("DEBUG: Other nutrients sum: " + otherSum + "g");
        }
        
        return topNutrients;
    }

    private double convertToGrams(double value, String unit) {
        if (unit == null) return value; // Assume grams if no unit
        
        return switch (unit.toLowerCase()) {
            case "g" -> value;                    // Already in grams
            case "mg" -> value / 1000.0;         // Milligrams to grams
            case "µg", "ug", "mcg" -> value / 1000000.0; // Micrograms to grams
            case "iu" -> value / 1000.0;         // International Units (rough conversion)
            case "re" -> value / 1000.0;         // Retinol Equivalents (rough conversion)
            case "dfe" -> value / 1000.0;        // Dietary Folate Equivalents (rough conversion)
            case "nfe" -> value / 1000.0;        // Niacin Equivalents (rough conversion)
            case "te" -> value / 1000.0;         // Tocopherol Equivalents (rough conversion)
            case "kcal", "cal" -> value / 1000.0; // Calories (treat as grams for visualization)
            case "kj" -> value / 4184.0;         // Kilojoules to grams (rough energy conversion)
            case "ne" -> value / 1000.0;         // Niacin Equivalent (mg to g)
            default -> {
                System.out.println("DEBUG: Unknown unit '" + unit + "', treating as grams");
                yield value;
            }
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

    private JPanel createSummaryPanel(NutrientStatistics stats) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.X_AXIS)); // Horizontal layout for bottom
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setPreferredSize(new Dimension(600, 90)); // Shorter summary panel
        // Show top 4 nutrients by weight
        List<Map.Entry<String, Double>> topNutrients = stats.getNutrientTotals().entrySet().stream()
                .filter(entry -> !entry.getKey().equals("Other nutrients"))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(4)
                .toList();
        for (Map.Entry<String, Double> entry : topNutrients) {
            String nutrientName = entry.getKey();
            double percentage = stats.getNutrientPercentages().get(nutrientName);
            double weight = entry.getValue();
            JLabel label = new JLabel(String.format(
                    "<html><b>%s</b><br/>%.1f%% (%.2fg)</html>", 
                    nutrientName, percentage, weight
            ));
            label.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            summaryPanel.add(label);
        }
        summaryPanel.add(Box.createHorizontalStrut(20));
        JLabel mealCountLabel = new JLabel("Total Meals: " + stats.getMealCount());
        mealCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        summaryPanel.add(mealCountLabel);
        return summaryPanel;
    }
} 