package swaps.view;


import javax.swing.*;
import java.awt.*;

public class SwapsView extends JPanel {
    private int step = 1;
    private DropdownOptionGoalType selectedGoalType = DropdownOptionGoalType.PRECISE;

    protected CardLayout swapsCardLayout;
    protected JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;

    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    public SwapsView() {
        swapsCardLayout = new CardLayout();
        swapSteps = new JPanel(swapsCardLayout);

        JPanel selectTypePanel = new JPanel();
        selectTypePanel.add(new JLabel("Select Goal Type"));
        DropdownOptionGoalType[] choices = {DropdownOptionGoalType.PRECISE, DropdownOptionGoalType.IMPRECISE};
        JComboBox goalTypeChoices = new JComboBox<>(choices);
        goalTypeChoices.addActionListener(e -> selectedGoalType = (DropdownOptionGoalType) goalTypeChoices.getSelectedItem());
        selectTypePanel.add(goalTypeChoices);
        swapSteps.add(selectTypePanel, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());

        JPanel createPreciseGoal = new JPanel();
        createPreciseGoal.add(new JLabel("Create Precise Goal"));
        swapSteps.add(createPreciseGoal, SwapWorkflowStep.PRECISE_GOAL_DETAILS.toString());

        JPanel createImpreciseGoal = new JPanel();
        createImpreciseGoal.add(new JLabel("Create Imprecise Goal"));
        DropdownOptionIntensity[] dropdownChoiceIntensityOptions = {DropdownOptionIntensity.HIGH, DropdownOptionIntensity.MEDIUM, DropdownOptionIntensity.LOW};
        JComboBox intensityComboBox = new JComboBox<>(dropdownChoiceIntensityOptions);
        createImpreciseGoal.add(intensityComboBox);
        swapSteps.add(createImpreciseGoal, SwapWorkflowStep.IMPRECISE_GOAL_DETAILS.toString());

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
                swapsCardLayout.show(swapSteps, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());
                break;
            }
            case 2: {
                String newPanelId = selectedGoalType.equals(DropdownOptionGoalType.PRECISE)
                        ? SwapWorkflowStep.PRECISE_GOAL_DETAILS.toString()
                        : SwapWorkflowStep.IMPRECISE_GOAL_DETAILS.toString();
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
