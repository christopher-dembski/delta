package swaps.view;

public enum DropDownCreateSecondGoal {
    YES("Yes"),
    NO("No");

    private final String label;

    DropDownCreateSecondGoal(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
