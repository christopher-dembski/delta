package meals.ui;


import meals.models.food.Food;

public record FoodSearchBoxOption(Food food) {
   @Override
    public String toString() {
       return food.getFoodDescription();
   }
}
