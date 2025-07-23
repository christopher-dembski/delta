package meals.models.food;

import shared.AppBackend;
import data.*;
import meals.models.nutrient.Nutrient;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * DAO for the existing Food.java class.
 * Only fetches and constructs the data needed for Food's specific fields:
 * - foodId, foodDescription
 * - FoodGroup (aggregated)
 * - Map<Nutrient, Float> nutrientAmounts (aggregated)
 * - List<Measure> possibleMeasures (aggregated)
 */
public class FoodDAO {
    private final String foodsTable = "foods";  // Use actual table name from CSV import
    
    // Helper DAOs for building aggregated relationships
    private final FoodGroupDAO foodGroupDAO;  // Using compatible FoodGroupDAO
    private final NutrientAmountDAOV2 nutrientAmountDAO;
    private final NutrientDAOV2 nutrientDAO;

    public FoodDAO() {
        this.foodGroupDAO = new FoodGroupDAO();  // Compatible with existing FoodGroup
        this.nutrientAmountDAO = new NutrientAmountDAOV2();
        this.nutrientDAO = new NutrientDAOV2();
    }

    /**
     * Find all foods with their complete aggregated data.
     */
    public List<Food> findAll() throws DatabaseException {
        List<IRecord> foodRecords = AppBackend.db().execute(new SelectQuery(foodsTable));
        return foodRecords.stream()
                .map(this::buildFoodFromRecord)
                .collect(Collectors.toList());
    }

    /**
     * Find a specific food by ID with all aggregated data.
     */
    public Food findById(Integer foodId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(foodsTable)
                        .filter("FoodID", Comparison.EQUAL, foodId)  // Use correct column name
        );
        return records.isEmpty() ? null : buildFoodFromRecord(records.get(0));
    }

    /**
     * Count total foods.
     */
    public int count() throws DatabaseException {
        return findAll().size();
    }

    /**
     * Build a complete Food object from a database record.
     * This method constructs all the aggregated relationships.
     */
    private Food buildFoodFromRecord(IRecord record) {
        try {
            // Extract basic food data (only what Food.java needs)
            Integer foodId = (Integer) record.getValue("FoodID");
            String foodDescription = (String) record.getValue("FoodDescription");
            Integer foodGroupId = (Integer) record.getValue("FoodGroupID");

            // Build aggregated FoodGroup
            FoodGroup foodGroup = buildFoodGroup(foodGroupId);

            // Build aggregated nutrient amounts Map<Nutrient, Float>
            Map<Nutrient, Float> nutrientAmounts = buildNutrientAmounts(foodId);

            // Build aggregated possible measures List<Measure>
            List<Measure> possibleMeasures = buildPossibleMeasures(foodId);

            return new Food(foodId, foodDescription, foodGroup, nutrientAmounts, possibleMeasures);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build Food from record: " + e.getMessage(), e);
        }
    }

    /**
     * Build FoodGroup object from food group ID.
     */
    private FoodGroup buildFoodGroup(Integer foodGroupId) throws DatabaseException {
        if (foodGroupId == null) return null;
        
        return foodGroupDAO.findById(foodGroupId);
    }

    /**
     * Build Map<Nutrient, Float> for this food's nutrient amounts.
     */
    private Map<Nutrient, Float> buildNutrientAmounts(Integer foodId) throws DatabaseException {
        Map<Nutrient, Float> nutrientAmounts = new HashMap<>();

        // Get all nutrient amounts for this food (more efficient method)
        List<NutrientAmountV2> amounts = nutrientAmountDAO.findByFood(foodId);

        // Convert to Map<Nutrient, Float>
        for (NutrientAmountV2 amount : amounts) {
            NutrientV2 nutrientV2 = nutrientDAO.findById(amount.getNutrientId());
            if (nutrientV2 != null) {
                // Convert NutrientV2 to Nutrient (adapt to existing class)
                Nutrient nutrient = new Nutrient(
                        nutrientV2.getNutrientId(),
                        nutrientV2.getNutrientSymbol(),
                        nutrientV2.getNutrientName(),
                        nutrientV2.getNutrientUnit()
                );
                
                // Add to map with Float value
                Float value = amount.getNutrientValue() != null ? amount.getNutrientValue().floatValue() : 0.0f;
                nutrientAmounts.put(nutrient, value);
            }
        }

        return nutrientAmounts;
    }

    /**
     * Build List<Measure> for possible measures for this food.
     * Note: This would require a food-measure relationship table or conversion factors.
     * For now, returning empty list - can be enhanced later.
     */
    private List<Measure> buildPossibleMeasures(Integer foodId) throws DatabaseException {
        // TODO: Implement when we have food-measure relationships
        // For now, return empty list to satisfy Food constructor
        return new ArrayList<>();
    }

    /**
     * Find foods by food group.
     */
    public List<Food> findByFoodGroup(Integer foodGroupId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(foodsTable)
                        .filter("FoodGroupID", Comparison.EQUAL, foodGroupId)  // Use correct column name
        );
        return records.stream()
                .map(this::buildFoodFromRecord)
                .collect(Collectors.toList());
    }

    /**
     * Find foods by description (partial match).
     */
    public List<Food> findByDescription(String description) throws DatabaseException {
        return findAll().stream()
                .filter(food -> food.getFoodDescription() != null && 
                              food.getFoodDescription().toLowerCase().contains(description.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "FoodDAO(table: " + foodsTable + ")";
    }
} 