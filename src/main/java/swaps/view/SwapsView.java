package swaps.view;

import javax.swing.*;
import java.awt.*;

public class SwapsView extends JFrame {
    private int step = 1;
    private GoalTypeOption selectedGoalType = GoalTypeOption.PRECISE;

    private enum GoalTypeOption {
        PRECISE("Precise"),
        IMPRECISE("Imprecise");

        private final String label;

        GoalTypeOption(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private enum Step {
        SELECT_GOAL_TYPE("Select Goal Type"),
        PRECISE_GOAL_DETAILS("Precise Goal Details"),
        IMPRECISE_GOAL_DETAILS("Imprecise Goal Details");

        private final String label;

        Step(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }


    public SwapsView() {
        setSize(1000, 1000);
        setVisible(true);

        CardLayout swapsCardLayout = new CardLayout();
        JPanel swapSteps = new JPanel(swapsCardLayout);

        JPanel selectTypePanel = new JPanel();
        selectTypePanel.add(new JLabel("Select Goal Type"));
        GoalTypeOption[] choices = {GoalTypeOption.PRECISE, GoalTypeOption.IMPRECISE};
        JComboBox goalTypeChoices = new JComboBox<>(choices);
        goalTypeChoices.addActionListener(e -> selectedGoalType = (GoalTypeOption) goalTypeChoices.getSelectedItem());
        selectTypePanel.add(goalTypeChoices);
        swapSteps.add(selectTypePanel, Step.SELECT_GOAL_TYPE.toString());

        JPanel createPreciseGoal = new JPanel();
        createPreciseGoal.add(new JLabel("Create Precise Goal"));
        swapSteps.add(createPreciseGoal, Step.PRECISE_GOAL_DETAILS.toString());

        JPanel createImpreciseGoal = new JPanel();
        createImpreciseGoal.add(new JLabel("Create Imprecise Goal"));
        swapSteps.add(createImpreciseGoal, Step.IMPRECISE_GOAL_DETAILS.toString());

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            step++;
            switch (step) {
                case 2: {
                    String newPanelId = selectedGoalType.equals(GoalTypeOption.PRECISE)
                            ? Step.PRECISE_GOAL_DETAILS.toString()
                            : Step.IMPRECISE_GOAL_DETAILS.toString();
                    swapsCardLayout.show(swapSteps, newPanelId);
                }
            }
        });

        // Layout
        this.setLayout(new BorderLayout());
        this.add(swapSteps, BorderLayout.NORTH);
        this.add(nextButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SwapsView();
    }
}
