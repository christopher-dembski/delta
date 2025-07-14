package swaps.ui.goals.create_goal_form.form_fields;

public enum DropdownOptionGoalIntensity {
    HIGH("A lot"),
    MEDIUM("A moderate amount"),
    LOW("A little");

    private final String label;

    DropdownOptionGoalIntensity(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
