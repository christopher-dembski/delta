package meals.models;

public class ConversionFactor {

    private final int id;
    private final String name;
    private final float conversionValue;

    /**
     * @param id              The id of the conversion factor.
     * @param name            The name of the conversion factor.
     * @param conversionValue The numeric value to multiply by for the conversion.
     */
    public ConversionFactor(Integer id, String name, Float conversionValue) {
        this.id = id;
        this.name = name;
        this.conversionValue = conversionValue;
    }

    /**
     * @return The unique identifier of the conversion factor.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The name of the conversion factor.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The numeric value to multiply by for the conversion.
     */
    public float getConversionValue() {
        return conversionValue;
    }
}
