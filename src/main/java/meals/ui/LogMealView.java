package meals.ui;


import meals.models.MockDataFactory;
import meals.models.food.Food;
import meals.models.food.Measure;
import shared.ui.searchable_list.SearchableListView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static shared.ui.searchable_list.UIConstants.*;

public class LogMealView extends JPanel {
    private static final String TITLE = "Log Meal";
    private static final String ADD_FOOD_BUTTON_LABEL = "Add Food";
    private static final String REMOVE_ITEM_BUTTON_LABEL = "Remove Item";
    private static final String CREATE_MEAL_BUTTON_LABEL = "Create Meal";
    private static final String QUANTITY_TEXT_BOX_LABEL = "Quantity: ";
    private static final String DEFAULT_QUANTITY_TEXT_FIELD_VALUE = "1.0";

    private final JButton addFoodButton;
    private final JButton removeFoodButton;
    private SearchableListView<FoodSearchBoxOption> foodSearchBox;
    private final JComboBox<Measure> measureOptions;
    private DefaultListModel<SelectedFoodListItem> selectedFoodListModel;
    private JList<SelectedFoodListItem> selectedFoodList;
    private JPanel quantityFormFieldContainer;
    private JTextField quantityField;
    private final JButton createMealButton;

    public LogMealView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addFoodButton = new JButton(ADD_FOOD_BUTTON_LABEL);
        removeFoodButton = new JButton(REMOVE_ITEM_BUTTON_LABEL);
        createMealButton = new JButton(CREATE_MEAL_BUTTON_LABEL);
        measureOptions = new JComboBox<>();
        measureOptions.setMaximumSize(new Dimension(DEFAULT_TEXT_FIELD_WIDTH, DEFAULT_TEXT_FIELD_HEIGHT));
        initSelectedFoodList();
        initQuantityField();
        initFoodSearchBox();
        initComponents();
    }

    private void initComponents() {
        this.add(new JLabel(TITLE));
        this.add(quantityFormFieldContainer);
        this.add(measureOptions);
        this.add(foodSearchBox);
        this.add(selectedFoodList);
        this.add(addFoodButton);
        this.add(removeFoodButton);
        this.add(createMealButton);
    }

    private void initQuantityField() {
        quantityFormFieldContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        quantityField = new JTextField(DEFAULT_QUANTITY_TEXT_FIELD_VALUE);
        quantityField.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELD_WIDTH, DEFAULT_TEXT_FIELD_HEIGHT));
        quantityField.setMaximumSize(new Dimension(DEFAULT_TEXT_FIELD_WIDTH, DEFAULT_TEXT_FIELD_HEIGHT));
        quantityFormFieldContainer.add(new JLabel(QUANTITY_TEXT_BOX_LABEL));
        quantityFormFieldContainer.add(quantityField);
    }

    private void initFoodSearchBox() {
        // TO DO: use food query service instead of mock food when service implemented
        // and move this logic to the presenter
        // may need to modify the search box class to support setting the list of items
        List<Food> foodList = MockDataFactory.generateMockFoods();
        List<FoodSearchBoxOption> foodSearchBoxOptions = foodList.stream().map(FoodSearchBoxOption::new).toList();
        foodSearchBox = new SearchableListView<>(foodSearchBoxOptions);
    }

    private void initSelectedFoodList() {
        selectedFoodListModel = new DefaultListModel<>();
        selectedFoodList = new JList<>(selectedFoodListModel);
    }

    protected void registerAddFoodListener(Runnable listener) {
        addFoodButton.addActionListener((e) -> {
            listener.run();
        });
    }

    protected FoodSearchBoxOption getSelectedFood() {
        return foodSearchBox.getSelectedItem();
    }

    protected void addSelectedFood(SelectedFoodListItem selectedFoodListItem) {
        selectedFoodListModel.addElement(selectedFoodListItem);
    }

    protected Float getSelectedQuantity() {
        float quantityFromFormField;
        try {
            quantityFromFormField = Float.parseFloat(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return quantityFromFormField > 0 ? quantityFromFormField : null;
    }

    protected void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, ERROR_FLASH_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    protected void registerRemoveItemListener(Runnable listener) {
        removeFoodButton.addActionListener((e) -> {
            listener.run();
        });
    }

    protected List<SelectedFoodListItem> getSelectedItemsAddedToMeal() {
        List<SelectedFoodListItem> list = new ArrayList<>();
        for (int i = 0; i < selectedFoodListModel.size(); i ++) {
            list.add(selectedFoodListModel.getElementAt(i));
        }
        return list;
    }

    protected SelectedFoodListItem getSelectedFoodAddedToMeal() {
        return selectedFoodList.getSelectedValue();
    }

    protected void removeItem(SelectedFoodListItem selectedFoodListItem) {
        selectedFoodListModel.removeElement(selectedFoodListItem);
    }

    protected SearchableListView<FoodSearchBoxOption> getFoodSearchBox() {
        return foodSearchBox;
    }

    protected Measure getSelectedMeasure() {
        return (Measure) measureOptions.getSelectedItem();
    }

    protected void setAvailableMeasures(List<Measure> measures) {
        measureOptions.removeAllItems();
        for (Measure measure: measures) {
            measureOptions.addItem(measure);
        }
        if (!measures.isEmpty()) {
            measureOptions.setSelectedItem(measures.getFirst());
        }
    }

    protected void registerCreateMealButtonListener(Runnable listener) {
        createMealButton.addActionListener((e) -> listener.run());
    }
}
