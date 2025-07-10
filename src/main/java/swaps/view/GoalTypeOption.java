package swaps.view;

public enum GoalTypeOption {
    PRECISE("Precise"),
    IMPRECISE("Imprecise");

    private final String label;

    GoalTypeOption(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
