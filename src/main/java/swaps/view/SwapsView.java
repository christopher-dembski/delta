package swaps.view;


import javax.swing.*;
import java.awt.*;

public class SwapsView extends JPanel {
    private int step = 1;
    private GoalTypeOption selectedGoalType = GoalTypeOption.PRECISE;

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

    private enum IntensityChoices {
        HIGH("A lot"),
        MEDIUM("A moderate amount"),
        LOW("A little");

        private final String label;

        IntensityChoices(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    protected CardLayout swapsCardLayout;
    protected JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;

    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    public SwapsView() {
        setSize(1000, 1000);
        setVisible(true);

        swapsCardLayout = new CardLayout();
        swapSteps = new JPanel(swapsCardLayout);

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
        IntensityChoices[] intensityChoices = {IntensityChoices.HIGH, IntensityChoices.MEDIUM, IntensityChoices.LOW};
        JComboBox intensityComboBox = new JComboBox<>(intensityChoices);
        createImpreciseGoal.add(intensityComboBox);
        swapSteps.add(createImpreciseGoal, Step.IMPRECISE_GOAL_DETAILS.toString());

        /* TO DO: extract buttons into separate method */

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            step++;
            handleStepChange();
        });
        nextButton.setPreferredSize(new Dimension(100, 30));

        // previous button
        previousButton = new JButton("Previous");
        previousButton.setEnabled(false); // cannot navigate back from initial step
        previousButton.addActionListener(e -> {
            step--;
            handleStepChange();
        });
        previousButton.setPreferredSize(new Dimension(100, 30));

        // wrap buttons in container
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout());
        buttonContainer.add(previousButton);
        buttonContainer.add(nextButton);

        // layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(swapSteps);
        this.add(buttonContainer);
    }

    private void handleStepChange() {
        previousButton.setEnabled(step != FIRST_STEP);
        nextButton.setEnabled(step != LAST_STEP);
        switch (step) {
            case 1: {
                swapsCardLayout.show(swapSteps, Step.SELECT_GOAL_TYPE.toString());
                break;
            }
            case 2: {
                String newPanelId = selectedGoalType.equals(GoalTypeOption.PRECISE)
                        ? Step.PRECISE_GOAL_DETAILS.toString()
                        : Step.IMPRECISE_GOAL_DETAILS.toString();
                swapsCardLayout.show(swapSteps, newPanelId);
                break;
            }
        }
    }

    public static void main(String[] args) {
        // temporary main method for testing
        // this component would be rendered within the main page
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.add(new SwapsView());
        frame.setVisible(true);
    }
}
