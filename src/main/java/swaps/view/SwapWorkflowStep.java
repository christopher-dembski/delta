package swaps.view;

public enum SwapWorkflowStep {
    SELECT_GOAL_TYPE("Select Goal Type"),
    CREATE_GOAL_1_PRECISE("Precise Goal Details"),
    CREATE_GOAL_1_IMPRECISE("Imprecise Goal Details"),
    CHOOSE_ONE_OR_TWO_GOALS("Choose One or Two Goals"),
    SELECT_GOAL_2_TYPE("Select Goal 2 Type"),
    CREATE_GOAL_2_PRECISE("Precise Goal Details 2"),
    CREATE_GOAL_2_IMPRECISE("Imprecise Goal Details 2");

    private final String label;

    SwapWorkflowStep(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
