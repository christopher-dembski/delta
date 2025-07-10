package swaps.view;

public enum OptionGoalType {
    PRECISE("Precise"),
    IMPRECISE("Imprecise");

    private final String label;

    OptionGoalType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
