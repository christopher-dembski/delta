package meals.models.food;

import data.IRecord;

/**
 * Represents a food group entity (e.g., "Dairy and Egg Products", "Fruits and fruit juices").
 * This class implements IRecord to integrate with the database layer.
 */
public class FoodGroup {
    private static final String TABLE_NAME = "food_groups";

    private Integer foodGroupId;
    private String foodGroupName;

    public static String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor for creating a food group with all details.
     */
    public FoodGroup(Integer foodGroupId, String foodGroupName) {
        this.foodGroupId = foodGroupId;
        this.foodGroupName = foodGroupName;
    }

    public FoodGroup(IRecord record) {
        this.foodGroupId = (int) record.getValue("id");
        this.foodGroupName = (String) record.getValue("name");
    }

    // Getters and setters
    public Integer getFoodGroupId() {
        return foodGroupId;
    }

    public String getFoodGroupName() {
        return foodGroupName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodGroup that = (FoodGroup) obj;
        return foodGroupId != null ? foodGroupId.equals(that.foodGroupId) : that.foodGroupId == null;
    }

    @Override
    public int hashCode() {
        return foodGroupId != null ? foodGroupId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FoodGroup(%s)".formatted(foodGroupName);
    }
}
