package swaps.view.goals;

import javax.swing.*;

public class DefineGoalsView extends JPanel {
    private static final String GOAL_1_HEADER = "Goal 1";
    private static final String GOAL_2_HEADER = "Goal 2";

    private final FormFieldDefineSecondGoal defineSecondGoalCheckbox;
    private final GoalsFormView goal1View;
    private final GoalsFormView goal2View;

    public DefineGoalsView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // goal 1
        goal1View = new GoalsFormView(GOAL_1_HEADER);
        this.add(goal1View);
        // define second goal checkbox
        defineSecondGoalCheckbox = new FormFieldDefineSecondGoal();
        this.add(defineSecondGoalCheckbox);
        // goal 2
        goal2View = new GoalsFormView(GOAL_2_HEADER);
        this.add(goal2View);
    }

    /* Conditional Rendering */

    public void setGoal2FormVisibility(boolean isVisible) {
        goal2View.setVisible(isVisible);
    }

    /* Form Fields */

    public FormFieldDefineSecondGoal getDefineSecondGoalCheckbox() {
        return defineSecondGoalCheckbox;
    }

    /* Subcomponents */

    public GoalsFormView getGoal1View() {
        return goal1View;
    }

    public GoalsFormView getGoal2View() {
        return goal2View;
    }
}
