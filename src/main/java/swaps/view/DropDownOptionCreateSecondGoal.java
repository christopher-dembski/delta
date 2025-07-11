package swaps.view;

public enum DropDownOptionCreateSecondGoal {
    YES("Yes"),
    NO("No");

    private final String label;

    DropDownOptionCreateSecondGoal(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
