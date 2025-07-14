package swaps.ui.goals.create_goal_form.form_fields;

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
