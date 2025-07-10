package swaps.view;

public enum SwapWorkflowStep {
    SELECT_GOAL_TYPE("Select Goal Type"),
    PRECISE_GOAL_DETAILS("Precise Goal Details"),
    IMPRECISE_GOAL_DETAILS("Imprecise Goal Details");

    private final String label;

    SwapWorkflowStep(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
