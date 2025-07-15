package data;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a nutrient amount for a specific food.
 * This stores the actual nutritional values.
 */
public class NutrientAmount implements IRecord {
    private Integer foodId;
    private Integer nutrientId;
    private Double nutrientValue;
    private Double standardError;
    private Integer numberOfObservations;
    private Integer nutrientSourceId;

    /**
     * Default constructor.
     */
    public NutrientAmount() {
    }

    /**
     * Constructor for basic nutrient amount.
     */
    public NutrientAmount(Integer foodId, Integer nutrientId, Double nutrientValue) {
        this.foodId = foodId;
        this.nutrientId = nutrientId;
        this.nutrientValue = nutrientValue;
    }

    /**
     * Complete constructor.
     */
    public NutrientAmount(Integer foodId, Integer nutrientId, Double nutrientValue, 
                         Double standardError, Integer numberOfObservations, Integer nutrientSourceId) {
        this.foodId = foodId;
        this.nutrientId = nutrientId;
        this.nutrientValue = nutrientValue;
        this.standardError = standardError;
        this.numberOfObservations = numberOfObservations;
        this.nutrientSourceId = nutrientSourceId;
    }

    /**
     * Constructor for creating a NutrientAmount from database record data.
     */
    public NutrientAmount(IRecord record) {
        this.foodId = (Integer) record.getValue("FoodID");
        this.nutrientId = (Integer) record.getValue("NutrientID");
        this.nutrientValue = (Double) record.getValue("NutrientValue");
        this.standardError = (Double) record.getValue("StandardError");
        this.numberOfObservations = (Integer) record.getValue("NumberofObservations");
        this.nutrientSourceId = (Integer) record.getValue("NutrientSourceID");
    }

    // Getters and setters
    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(Integer nutrientId) {
        this.nutrientId = nutrientId;
    }

    public Double getNutrientValue() {
        return nutrientValue;
    }

    public void setNutrientValue(Double nutrientValue) {
        this.nutrientValue = nutrientValue;
    }

    public Double getStandardError() {
        return standardError;
    }

    public void setStandardError(Double standardError) {
        this.standardError = standardError;
    }

    public Integer getNumberOfObservations() {
        return numberOfObservations;
    }

    public void setNumberOfObservations(Integer numberOfObservations) {
        this.numberOfObservations = numberOfObservations;
    }

    public Integer getNutrientSourceId() {
        return nutrientSourceId;
    }

    public void setNutrientSourceId(Integer nutrientSourceId) {
        this.nutrientSourceId = nutrientSourceId;
    }

    // Domain logic methods
    public boolean hasSignificantValue() {
        return nutrientValue != null && nutrientValue > 0;
    }

    public boolean hasQualityData() {
        return numberOfObservations != null && numberOfObservations > 0;
    }

    public Double getValueForServing(Double conversionFactor) {
        if (nutrientValue == null || conversionFactor == null) return null;
        return nutrientValue * conversionFactor;
    }

    // IRecord implementation
    @Override
    public Object getValue(String field) {
        return switch (field) {
            case "FoodID" -> foodId;
            case "NutrientID" -> nutrientId;
            case "NutrientValue" -> nutrientValue;
            case "StandardError" -> standardError;
            case "NumberofObservations" -> numberOfObservations;
            case "NutrientSourceID" -> nutrientSourceId;
            default -> null;
        };
    }

    @Override
    public Collection<String> fieldNames() {
        return Map.of("FoodID", foodId, "NutrientID", nutrientId, 
                     "NutrientValue", nutrientValue, "StandardError", standardError,
                     "NumberofObservations", numberOfObservations, "NutrientSourceID", nutrientSourceId).keySet();
    }

    @Override
    public String toString() {
        return "NutrientAmount(foodId: %d, nutrientId: %d, value: %s)"
            .formatted(foodId, nutrientId, nutrientValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NutrientAmount that = (NutrientAmount) obj;
        return foodId != null && foodId.equals(that.foodId) &&
               nutrientId != null && nutrientId.equals(that.nutrientId);
    }

    @Override
    public int hashCode() {
        int result = foodId != null ? foodId.hashCode() : 0;
        result = 31 * result + (nutrientId != null ? nutrientId.hashCode() : 0);
        return result;
    }
} 