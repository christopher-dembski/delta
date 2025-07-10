package swaps.view;

public enum DropdownOptionGoalType {
    PRECISE("Precise"),
    IMPRECISE("Imprecise");

    private final String label;

    DropdownOptionGoalType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
