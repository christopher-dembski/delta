package meals.models.meal;

import meals.models.food.ConversionFactor;
import meals.models.food.Food;

/**
 * Represents an item that is part of a meal.
 */
public class MealItem {

    private int id;
    private Food food;
    private ConversionFactor conversionFactor;
    // given conversion factors like "10 Chips", "1 Head of Lettuce", "10 mL"
    // a quantity of 5 would represent "50 chips", "5 Heads of Lettuce", "50 mL"
    private float quantity;

    /**
     * @param id               The ID of the meal item.
     * @param food             The food associated with the meal item.
     * @param quantity         The quantity of the food for this meal item.
     * @param conversionFactor The selected conversion factor for the meal item. (ex. "10 Chips")
     */
    public MealItem(Integer id, Food food, Float quantity, ConversionFactor conversionFactor) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.conversionFactor = conversionFactor;
    }

    /**
     * @return The ID of the meal item.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The food associated with the meal item.
     */
    public Food getFood() {
        return food;
    }

    /**
     * @return The quantity of the food for this meal item.
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     * @return The selected conversion factor for the meal item. (ex. "10 Chips")
     */
    public ConversionFactor getConversionFactor() {
        return this.conversionFactor;
    }
}
