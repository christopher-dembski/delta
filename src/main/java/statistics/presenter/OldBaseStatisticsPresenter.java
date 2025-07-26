// package statistics.presenter;

// import data.*;
// import meals.models.meal.Meal;

// import javax.swing.*;
// import java.awt.*;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// public abstract class OldBaseStatisticsPresenter {
//     protected final FoodDAO foodDAO;
//     protected final NutrientAmountDAO nutrientAmountDAO;
//     protected final NutrientDAO nutrientDAO;

//     public OldBaseStatisticsPresenter() {
//         this.foodDAO = new FoodDAO(shared.AppBackend.db(), new FoodGroupDAO(shared.AppBackend.db()));
//         this.nutrientAmountDAO = new NutrientAmountDAO(shared.AppBackend.db());
//         this.nutrientDAO = new NutrientDAO(shared.AppBackend.db());
//     }

//     public final JPanel presentStatistics(String startDate, String endDate) {
//         try {
//             List<SampleMeal> sampleMeals = createSampleMeals();
//             System.out.println("DEBUG: Found " + sampleMeals.size() + " sample meals");
//             if (sampleMeals.isEmpty()) {
//                 return createNoDataPanel("No data or incorrect date.");
//             }
//             Object statisticsData = calculateStatistics(sampleMeals);
//             JPanel chartPanel = createVisualization(statisticsData);
//             return wrapWithDateInfo(chartPanel, sampleMeals.size());
//         } catch (Exception e) {
//             e.printStackTrace();
//             return createErrorPanel("Error generating statistics: " + e.getMessage());
//         }
//     }

//     protected abstract Object calculateStatistics(List<SampleMeal> sampleMeals) throws Exception;

//     protected abstract JPanel createVisualization(Object statisticsData);

//     protected List<Food> fetchAllFoods() throws Exception {
//         return foodDAO.findAll();
//     }

//     protected List<NutrientAmount> fetchAllNutrientAmounts() throws Exception {
//         return nutrientAmountDAO.findAll();
//     }

//     protected List<Nutrient> fetchAllNutrients() throws Exception {
//         return nutrientDAO.findAll();
//     }

//     private List<SampleMeal> createSampleMeals() {
//         List<SampleMeal> sampleMeals = new ArrayList<>();
//         LocalDate[] dates = {
//             LocalDate.of(2024, 1, 15),
//             LocalDate.of(2024, 1, 16), 
//             LocalDate.of(2024, 1, 17)
//         };
//         String[] mealTypes = {"breakfast", "lunch", "dinner", "snack"};
//         Integer[] foodIds = {5, 7, 8, 10, 61, 129, 415, 1415, 2873, 2880, 2885, 5573, 5574, 5575, 5580, 5582, 5585, 5586, 5587, 5589};
//         for (LocalDate date : dates) {
//             for (String mealType : mealTypes) {
//                 sampleMeals.add(new SampleMeal(date, mealType, foodIds[sampleMeals.size() % foodIds.length], 100.0));
//             }
//         }
//         System.out.println("DEBUG: Created " + sampleMeals.size() + " sample meals");
//         return sampleMeals;
//     }

//     protected JPanel createErrorPanel(String errorMessage) {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBorder(BorderFactory.createTitledBorder("Statistics Error"));
//         JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
//                 "<h2>❌ Error</h2>" +
//                 "<p>" + errorMessage + "</p>" +
//                 "</div></html>");
//         errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
//         panel.add(errorLabel, BorderLayout.CENTER);
//         return panel;
//     }

//     // Refactored: createNoDataPanel now takes a message string
//     protected JPanel createNoDataPanel(String message) {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setBorder(BorderFactory.createTitledBorder("No Data Found"));
//         JLabel noDataLabel = new JLabel("<html><div style='text-align: center;'>" +
//                 "<h2>📊 No Data Available</h2>" +
//                 "<p>" + message + "</p>" +
//                 "<p>Please select a different date range or add some meal data.</p>" +
//                 "</div></html>");
//         noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
//         panel.add(noDataLabel, BorderLayout.CENTER);
//         return panel;
//     }

//     // Refactored: wrapWithDateInfo now just takes mealCount
//     protected JPanel wrapWithDateInfo(JPanel chartPanel, int mealCount) {
//         JPanel wrapperPanel = new JPanel(new BorderLayout());
//         JLabel infoLabel = new JLabel("📅 Meals: " + mealCount);
//         infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
//         infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
//         wrapperPanel.add(infoLabel, BorderLayout.NORTH);
//         wrapperPanel.add(chartPanel, BorderLayout.CENTER);
//         return wrapperPanel;
//     }

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
