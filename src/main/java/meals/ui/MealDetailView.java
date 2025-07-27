package meals.ui;

import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.models.food.Food;
import meals.models.nutrient.Nutrient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class MealDetailView extends JPanel {
    private static final String TITLE = "Meal Details";
    private static final String BACK_BUTTON_LABEL = "← Back to Meal List";
    
    // Static field to store the selected meal from meal list
    public static Meal selectedMeal;
    
    private Meal currentMeal;
    private JLabel titleLabel;
    private JPanel mealInfoPanel;
    private JPanel foodItemsPanel;
    private JPanel nutritionPanel;
    private JButton backButton;
    
    public MealDetailView() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1200, 700)); // Set preferred size
        this.setMinimumSize(new Dimension(1000, 600));   // Set minimum size to prevent overlap
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel(TITLE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titlePanel.add(titleLabel);
        
        // Back button
        backButton = new JButton(BACK_BUTTON_LABEL);
        titlePanel.add(backButton);
        
        this.add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel with proper sizing
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Meal info panel (left side) - fixed width
        mealInfoPanel = new JPanel();
        mealInfoPanel.setLayout(new BoxLayout(mealInfoPanel, BoxLayout.Y_AXIS));
        mealInfoPanel.setBorder(BorderFactory.createTitledBorder("Meal Information"));
        mealInfoPanel.setPreferredSize(new Dimension(200, 0));
        mealInfoPanel.setMinimumSize(new Dimension(200, 0));
        
        // Food items panel (center) - compact layout
        foodItemsPanel = new JPanel();
        foodItemsPanel.setLayout(new BoxLayout(foodItemsPanel, BoxLayout.Y_AXIS));
        foodItemsPanel.setBorder(BorderFactory.createTitledBorder("Food Items"));
        
        // Create scroll pane for food items with preferred size
        JScrollPane foodScrollPane = new JScrollPane(foodItemsPanel);
        foodScrollPane.setPreferredSize(new Dimension(400, 0));
        foodScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        foodScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Nutrition panel (right side) - fixed width
        nutritionPanel = new JPanel();
        nutritionPanel.setLayout(new BoxLayout(nutritionPanel, BoxLayout.Y_AXIS));
        nutritionPanel.setBorder(BorderFactory.createTitledBorder("Nutritional Information"));
        nutritionPanel.setPreferredSize(new Dimension(450, 0));
        nutritionPanel.setMinimumSize(new Dimension(450, 0));
        
        mainPanel.add(mealInfoPanel, BorderLayout.WEST);
        mainPanel.add(foodScrollPane, BorderLayout.CENTER);
        mainPanel.add(nutritionPanel, BorderLayout.EAST);
        
        this.add(mainPanel, BorderLayout.CENTER);
    }
    
    public void displayMeal(Meal meal) {
        this.currentMeal = meal;
        updateDisplay();
    }
    
    private void updateDisplay() {
        if (currentMeal == null) return;
        
        // Update title
        titleLabel.setText(TITLE + " - " + currentMeal.getMealType() + " (" + 
                         new java.text.SimpleDateFormat("HH:mm").format(currentMeal.getCreatedAt()) + ")");
        
        // Update meal info
        updateMealInfo();
        
        // Update food items
        updateFoodItems();
        
        // Clear nutrition panel initially
        nutritionPanel.removeAll();
        nutritionPanel.revalidate();
        nutritionPanel.repaint();
    }
    
    private void updateMealInfo() {
        mealInfoPanel.removeAll();
        
        // Meal type and time
        JLabel mealTypeLabel = new JLabel("Type: " + currentMeal.getMealType());
        JLabel timeLabel = new JLabel("Time: " + new java.text.SimpleDateFormat("HH:mm").format(currentMeal.getCreatedAt()));
        JLabel dateLabel = new JLabel("Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(currentMeal.getCreatedAt()));
        JLabel itemsLabel = new JLabel("Items: " + currentMeal.getMealItems().size());
        
        mealInfoPanel.add(mealTypeLabel);
        mealInfoPanel.add(Box.createVerticalStrut(5));
        mealInfoPanel.add(timeLabel);
        mealInfoPanel.add(Box.createVerticalStrut(5));
        mealInfoPanel.add(dateLabel);
        mealInfoPanel.add(Box.createVerticalStrut(5));
        mealInfoPanel.add(itemsLabel);
        
        mealInfoPanel.revalidate();
        mealInfoPanel.repaint();
    }
    
    private void updateFoodItems() {
        foodItemsPanel.removeAll();
        
        for (int i = 0; i < currentMeal.getMealItems().size(); i++) {
            MealItem item = currentMeal.getMealItems().get(i);
            JPanel itemPanel = createFoodItemPanel(item);
            foodItemsPanel.add(itemPanel);
            
            // Add smaller gap between items (except after last item)
            if (i < currentMeal.getMealItems().size() - 1) {
                foodItemsPanel.add(Box.createVerticalStrut(3));
            }
        }
        
        // Add glue to push items to top
        foodItemsPanel.add(Box.createVerticalGlue());
        
        foodItemsPanel.revalidate();
        foodItemsPanel.repaint();
    }
    
    private JPanel createFoodItemPanel(MealItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Limit height to 60px
        panel.setPreferredSize(new Dimension(0, 60)); // Set preferred height
        
        // Food info - more compact
        JLabel foodLabel = new JLabel(item.getFood().getFoodDescription());
        foodLabel.setFont(foodLabel.getFont().deriveFont(Font.BOLD, 12f)); // Smaller font
        
        JLabel quantityLabel = new JLabel(String.format("%.2f %s", item.getQuantity(), item.getSelectedMeasure().getName()));
        quantityLabel.setFont(quantityLabel.getFont().deriveFont(10f)); // Smaller font
        quantityLabel.setForeground(Color.GRAY);
        
        // Use FlowLayout for more compact display
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        infoPanel.add(foodLabel);
        infoPanel.add(new JLabel(" - "));
        infoPanel.add(quantityLabel);
        
        // Click instruction - smaller
        JLabel clickLabel = new JLabel("📊");
        clickLabel.setForeground(Color.BLUE);
        clickLabel.setFont(clickLabel.getFont().deriveFont(14f));
        clickLabel.setToolTipText("Click for nutrition info");
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(clickLabel, BorderLayout.EAST);
        
        // Add click listener
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNutritionInfo(item);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 240, 240));
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(null);
                panel.setCursor(Cursor.getDefaultCursor());
            }
        });
        
        return panel;
    }
    
    private void showNutritionInfo(MealItem item) {
        nutritionPanel.removeAll();
        
        Food food = item.getFood();
        Map<Nutrient, Float> nutrientAmounts = food.getNutrientAmounts();
        
        // Title
        JLabel titleLabel = new JLabel("Nutrition for: " + food.getFoodDescription());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        nutritionPanel.add(titleLabel);
        nutritionPanel.add(Box.createVerticalStrut(10));
        
        // Quantity info
        JLabel quantityLabel = new JLabel(String.format("Quantity: %.2f %s", item.getQuantity(), item.getSelectedMeasure().getName()));
        nutritionPanel.add(quantityLabel);
        nutritionPanel.add(Box.createVerticalStrut(10));
        
        // Create pie chart for main nutrients
        if (!nutrientAmounts.isEmpty()) {
            try {
                NutritionalPieChart pieChart = new NutritionalPieChart(nutrientAmounts, item.getQuantity());
                nutritionPanel.add(pieChart);
                nutritionPanel.add(Box.createVerticalStrut(10));
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Error creating chart: " + e.getMessage());
                errorLabel.setForeground(Color.RED);
                nutritionPanel.add(errorLabel);
            }
        }
        
        // Nutrient details table
        JPanel nutrientTablePanel = createNutrientTable(nutrientAmounts, item.getQuantity());
        nutritionPanel.add(nutrientTablePanel);
        
        nutritionPanel.revalidate();
        nutritionPanel.repaint();
    }
    
    private JPanel createNutrientTable(Map<Nutrient, Float> nutrientAmounts, float quantity) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nutrient Details"));
        
        // Filter out zero values and sort by amount (highest to lowest)
        java.util.List<Map.Entry<Nutrient, Float>> sortedNutrients = nutrientAmounts.entrySet().stream()
                .filter(entry -> (entry.getValue() * quantity) > 0.001) // Filter out essentially zero values
                .sorted((e1, e2) -> Float.compare(e2.getValue() * quantity, e1.getValue() * quantity)) // Sort by amount descending
                .collect(java.util.stream.Collectors.toList());
        
        // Create table model
        String[] columnNames = {"Nutrient", "Amount", "Unit"};
        Object[][] data = new Object[sortedNutrients.size()][3];
        
        for (int i = 0; i < sortedNutrients.size(); i++) {
            Map.Entry<Nutrient, Float> entry = sortedNutrients.get(i);
            Nutrient nutrient = entry.getKey();
            Float amount = entry.getValue() * quantity; // Adjust for quantity
            
            data[i][0] = nutrient.getNutrientName();
            data[i][1] = String.format("%.2f", amount);
            data[i][2] = nutrient.getNutrientUnit();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Nutrient name
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Amount
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Unit
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350, 250));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    public void registerBackButtonListener(Runnable listener) {
        backButton.addActionListener(e -> listener.run());
    }
} 