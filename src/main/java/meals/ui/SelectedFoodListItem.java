package meals.ui;


import meals.models.food.Food;
import meals.models.food.Measure;

public record SelectedFoodListItem(Food food, float quantity, Measure measure) {
    @Override
    public String toString() {
        return "%s %.2f %s".formatted(food.getFoodDescription(), quantity, measure.getName());
    }
}
