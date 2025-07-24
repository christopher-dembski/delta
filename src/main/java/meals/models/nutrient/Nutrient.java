package meals.models.nutrient;


import data.IRecord;

/**
 * Represents a nutrient entity (e.g., "PROTEIN", "CALCIUM", "VITAMIN C").
 * This class implements IRecord to integrate with the database layer.
 */
public class Nutrient {
    private static final String TABLE_NAME = "nutrients";

    private Integer nutrientId;
    private String nutrientSymbol;
    private String nutrientUnit;
    private String nutrientName;


    public static String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor for creating a nutrient with all details.
     */
    public Nutrient(Integer nutrientId,String nutrientSymbol, String nutrientUnit,
                    String nutrientName) {
        this.nutrientId = nutrientId;
        this.nutrientSymbol = nutrientSymbol;
        this.nutrientUnit = nutrientUnit;
        this.nutrientName = nutrientName;
    }

    public Nutrient(IRecord record) {
        this.nutrientId = (int) record.getValue("id");
        this.nutrientSymbol = (String) record.getValue("symbol");
        this.nutrientUnit = (String) record.getValue("unit");
        this.nutrientName = (String) record.getValue("unit");
    }

    // Getters and setters
    public Integer getNutrientId() {
        return nutrientId;
    }

    public String getNutrientSymbol() {
        return nutrientSymbol;
    }

    public String getNutrientUnit() {
        return nutrientUnit;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    // Domain logic methods
    public String getDisplayNameWithUnit() {
        String name = nutrientName != null ? nutrientName : "Unknown Nutrient";
        String unit = nutrientUnit != null ? " (" + nutrientUnit + ")" : "";
        return name + unit;
    }

    @Override
    public String toString() {
        return "Nutrient(id: %d, name: %s, unit: %s)".formatted(nutrientId, nutrientName, nutrientUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nutrient that = (Nutrient) obj;
        return nutrientId != null ? nutrientId.equals(that.nutrientId) : that.nutrientId == null;
    }

    @Override
    public int hashCode() {
        return nutrientId != null ? nutrientId.hashCode() : 0;
    }
}
