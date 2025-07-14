package swaps.view;

import javax.swing.*;
import java.awt.*;

public class GoalsFormView extends JPanel {
    // goal 1
    private FormFieldGoalType goal1TypeField;
    private FormFieldPreciseAmount goal1PlaceholderFIeld;
    private FormFieldGoalIntensity goal1IntensityField;
    // 1 or 2 goals
    private FormFieldChooseOneOrTwoGoals chooseOneOrTwoGoalsField;
    private boolean goal2SectionVisible;
    // goal 2
    private JPanel goal2Section;
    private FormFieldGoalType goal2TypeField;
    private FormFieldPreciseAmount goal2PreciseAmountPlaceholderField;
    private FormFieldGoalIntensity Goal2IntensityField;

    public GoalsFormView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initGoal1Section();
        initChooseOneOrTwoGoalsSection();
        initGoalTwoSection();
    }

    private void initGoal1Section() {
        JPanel goal1Section = new JPanel();
        goal1Section.setLayout(new BoxLayout(goal1Section, BoxLayout.Y_AXIS));
        goal1Section.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        goal1Section.add(new JLabel("Goal 1"));
        goal1TypeField = new FormFieldGoalType();
        goal1Section.add(goal1TypeField);
        goal1PlaceholderFIeld = new FormFieldPreciseAmount();
        goal1Section.add(goal1PlaceholderFIeld);
        goal1IntensityField = new FormFieldGoalIntensity(1);
        goal1Section.add(goal1IntensityField);
        this.add(goal1Section);
    }

    private void initChooseOneOrTwoGoalsSection() {
        chooseOneOrTwoGoalsField = new FormFieldChooseOneOrTwoGoals();
        this.add(chooseOneOrTwoGoalsField);
    }

    private void initGoalTwoSection() {
        goal2SectionVisible = false;
        goal2Section = new JPanel();
        goal2Section.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        goal2Section.setLayout(new BoxLayout(goal2Section, BoxLayout.Y_AXIS));
        goal2Section.add(new JLabel("Goal 2"));
        goal2TypeField = new FormFieldGoalType();
        goal2Section.add(goal2TypeField);
        goal2PreciseAmountPlaceholderField = new FormFieldPreciseAmount();
        goal2Section.add(goal2PreciseAmountPlaceholderField);
        Goal2IntensityField = new FormFieldGoalIntensity(2);
        goal2Section.add(Goal2IntensityField);
        goal2Section.setVisible(goal2SectionVisible);
        this.add(goal2Section);
    }

    public void setGoal2SectionVisibility(boolean isVisible) {
        goal2Section.setVisible(isVisible);
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
