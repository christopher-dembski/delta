package meals.models.meal;

import java.util.List;

/**
 * Represents a meal logged by the user.
 */
public class Meal {
    /**
     * The different types of meals.
     */
    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACK
    }

    private final int id;
    private final MealType mealType;
    private final List<MealItem> mealItems;

    /**
     * @param id        The unique identifier of the meal.
     * @param mealType  The type of meal.
     * @param mealItems The list of items for the meal (foods and their respective quantities).
     */
    public Meal(int id, MealType mealType, List<MealItem> mealItems) {
        this.id = id;
        this.mealType = mealType;
        this.mealItems = mealItems;
    }

    /**
     * @return The unique identifier of the meal.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The type of meal.
     */
    public MealType getMealType() {
        return mealType;
    }

    /**
     * @return The list of items for the meal (foods and their respective quantities).
     */
    public List<MealItem> getMealItems() {
        return mealItems;
    }
}
