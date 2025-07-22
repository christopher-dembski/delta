package swaps.models;

import meals.models.food.Food;

public record Swap(Food oldFood, Food newFood) {
}
