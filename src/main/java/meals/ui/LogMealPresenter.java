package meals.ui;


import meals.models.food.Food;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import meals.services.CreateMealService;

import java.util.*;

public class LogMealPresenter {
    private static final String INVALID_QUANTITY_MESSAGE = "Invalid quantity. Value must be a number > 0.";
    private static final String DUPLICATE_FOOD_MESSAGE = "Foods for a meal must be unique.";

    private final LogMealView view;

    public LogMealPresenter(LogMealView view) {
        this.view = view;
        view.registerAddFoodListener(this::addFoodListener);
        view.registerRemoveItemListener(this::removeItemListener);
        view.getFoodSearchBox().addSelectionChangedListener(option -> {
            Food food = option.food();
            view.setAvailableMeasures(food.getPossibleMeasures());
        });
        // set initial options for measures
        FoodSearchBoxOption initial = view.getSelectedFood();
        if (initial != null) {
            view.setAvailableMeasures(initial.food().getPossibleMeasures());
        }
        view.registerCreateMealButtonListener(this::createFood);
    }

    private void createFood() {
        // TO DO: add dropdown for meal type
        List<MealItem> mealItems = new ArrayList<>();
        for (SelectedFoodListItem selectedFoodListItem: view.getSelectedItemsAddedToMeal()) {
            mealItems.add(
                    new MealItem(new Random().nextInt(10000),
                            selectedFoodListItem.food(),
                            selectedFoodListItem.quantity(),
                            selectedFoodListItem.measure())
            );
        }
        Meal meal = new Meal(new Random().nextInt(10000), Meal.MealType.SNACK, mealItems, new Date());
        CreateMealService.instance().createMeal(meal);
    }

    private void addFoodListener() {
        Food selectedFood = view.getSelectedFood().food();
        Float quantity = view.getSelectedQuantity();
        if (quantity == null) {
            view.showErrorMessage(INVALID_QUANTITY_MESSAGE);
            return;
        }
        if (duplicateFood(selectedFood)) {
            view.showErrorMessage(DUPLICATE_FOOD_MESSAGE);
            return;
        }
        view.addSelectedFood(new SelectedFoodListItem(selectedFood, quantity, view.getSelectedMeasure()));
    }

    private boolean duplicateFood(Food food) {
        List<SelectedFoodListItem> currentFoods = view.getSelectedItemsAddedToMeal();
        for (SelectedFoodListItem lisItem: currentFoods) {
            // we can compare IDs, since we know foods are frozen records that are never updated in the database
            if (lisItem.food().getFoodId().equals(food.getFoodId())) {
                return true;
            }
        }
        return false;
    }

    private void removeItemListener() {
        SelectedFoodListItem selectedFoodListItem = view.getSelectedFoodAddedToMeal();
        view.removeItem(selectedFoodListItem);
    }
}
