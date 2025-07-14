package swaps.view;

public enum DropDownOptionCreateSecondGoal {
    YES("Yes"),
    NO("No");

    private final String label;

    DropDownOptionCreateSecondGoal(String label) {
        this.label = label;
    }

    public boolean toBoolean() {
        return this.equals(DropDownOptionCreateSecondGoal.YES);  // Yes -> True, No -> False
    }

    @Override
    public String toString() {
        return label;
    }
}
