package meals.models.food;

import data.DatabaseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DAO for the existing FoodGroup.java class.
 * Uses V2 architecture under the hood but returns FoodGroup objects
 * that are compatible with existing code.
 * 
 * This is an adapter that bridges between:
 * - Existing FoodGroup class (simple: id + name)
 * - V2 architecture (FoodGroupDAOV2 + FoodGroupV2)
 */
public class FoodGroupDAO {
    
    // Use V2 DAO under the hood
    private final FoodGroupDAOV2 foodGroupDAOV2;
    
    public FoodGroupDAO() {
        this.foodGroupDAOV2 = new FoodGroupDAOV2();
    }
    
    /**
     * Find all food groups, returning simple FoodGroup objects.
     */
    public List<FoodGroup> findAll() throws DatabaseException {
        List<FoodGroupV2> foodGroupsV2 = foodGroupDAOV2.findAll();
        return foodGroupsV2.stream()
                .map(this::convertToFoodGroup)
                .collect(Collectors.toList());
    }
    
    /**
     * Find food group by ID, returning simple FoodGroup object.
     */
    public FoodGroup findById(Integer foodGroupId) throws DatabaseException {
        FoodGroupV2 foodGroupV2 = foodGroupDAOV2.findById(foodGroupId);
        return foodGroupV2 != null ? convertToFoodGroup(foodGroupV2) : null;
    }
    
    /**
     * Count total food groups.
     */
    public int count() throws DatabaseException {
        return foodGroupDAOV2.count();
    }
    
    /**
     * Find food groups by name (partial match).
     */
    public List<FoodGroup> findByName(String name) throws DatabaseException {
        return findAll().stream()
                .filter(fg -> fg.getFoodGroupName() != null && 
                            fg.getFoodGroupName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Convert FoodGroupV2 to simple FoodGroup.
     * This adapter method bridges between V2 and existing classes.
     */
    private FoodGroup convertToFoodGroup(FoodGroupV2 foodGroupV2) {
        return new FoodGroup(
                foodGroupV2.getFoodGroupId(),
                foodGroupV2.getDisplayName()  // Use display name for compatibility
        );
    }
    
    @Override
    public String toString() {
        return "FoodGroupDAO(using V2 architecture)";
    }
} 