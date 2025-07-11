package swaps.view;

import javax.swing.*;

public class GoalsFormView extends JPanel {
    // goal 1
    private FormFieldGoalType goal1TypeField;
    private FormFieldPreciseAmount goal1PlaceholderFIeld;
    private FormFieldGoalIntensity goal1IntensityField;
    // 1 or 2 goals
    private FormFieldChooseOneOrTwoGoals chooseOneOrTwoGoalsField;
    // goal 2
    private FormFieldGoalType goal2TypeField;
    private FormFieldPreciseAmount goal2PreciseAmountPlaceholderField;
    private FormFieldGoalIntensity Goal2IntensityField;

    public GoalsFormView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(new JLabel("Goal 1"));
        goal1TypeField = new FormFieldGoalType();
        this.add(goal1TypeField);
        goal1PlaceholderFIeld = new FormFieldPreciseAmount();
        this.add(goal1PlaceholderFIeld);
        goal1IntensityField = new FormFieldGoalIntensity(1);
        this.add(goal1IntensityField);

        chooseOneOrTwoGoalsField = new FormFieldChooseOneOrTwoGoals();
        this.add(chooseOneOrTwoGoalsField);

        this.add(new JLabel("Goal 2"));
        goal2TypeField = new FormFieldGoalType();
        this.add(goal2TypeField);
        goal2PreciseAmountPlaceholderField = new FormFieldPreciseAmount();
        this.add(goal2PreciseAmountPlaceholderField);
        Goal2IntensityField = new FormFieldGoalIntensity(2);
        this.add(Goal2IntensityField);
    }

    public FormFieldGoalType getGoal1TypeField() {
        return goal1TypeField;
    }

    public FormFieldGoalIntensity getGoal1IntensityField() {
        return goal1IntensityField;
    }

    public FormFieldChooseOneOrTwoGoals getChooseOneOrTwoGoalsField() {
        return chooseOneOrTwoGoalsField;
    }

    public FormFieldGoalType getGoal2TypeField() {
        return goal2TypeField;
    }

    public FormFieldGoalIntensity getGoal2IntensityField() {
        return Goal2IntensityField;
    }

    public FormFieldPreciseAmount getGoal2PreciseAmountPlaceholderField() {
        return goal2PreciseAmountPlaceholderField;
    }
}
