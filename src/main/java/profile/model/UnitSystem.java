package profile.model;

public enum UnitSystem {
    METRIC("Metric (cm, kg)"),
    IMPERIAL("Imperial (ft, lb)");

    private final String displayName;

    UnitSystem(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
