package meals.models.meal;

import data.IRecord;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;


import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Represents a meal logged by the user.
 */
public class Meal implements IRecord {
    private static final String TABLE = "meals";

    /**
     * The different types of meals.
     */
    public enum MealType {
        BREAKFAST("Breakfast"),
        LUNCH("Lunch"),
        DINNER("Dinner"),
        SNACK("Snack");

        private final String label;

        MealType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public static String getTableName() {
        return TABLE;
    }

    private final int id;
    private final MealType mealType;
    private final List<MealItem> mealItems;
    private final Date createdAt;

    /**
     * @param id        The unique identifier of the meal.
     * @param mealType  The type of meal.
     * @param mealItems The list of items for the meal (foods and their respective quantities).
     */
    public Meal(int id, MealType mealType, List<MealItem> mealItems, Date createdAt) {
        this.id = id;
        this.mealType = mealType;
        this.mealItems = mealItems;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Object getValue(String field) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return switch (field) {
            case "id" -> id;
            case "meal_type" -> mealType.toString();
            case "user_id" -> 1; // TO DO: replace with current user
            case "created_on" -> new SimpleDateFormat("yyyy-MM-dd").format(createdAt);
            default -> null;
        };
    }

    @Override
    public Collection<String> fieldNames() {
        return List.of("id", "meal_type", "user_id", "created_on");
    }
}
