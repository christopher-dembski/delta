package swaps.view;


import javax.swing.*;
import java.awt.*;

public class SwapsView extends JPanel {
    // constants
    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    // application state
    private int currentStep = 1;
    private DropdownOptionGoalType selectedGoalType = DropdownOptionGoalType.PRECISE;

    // general layout components
    protected CardLayout swapsCardLayout;
    protected JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;
    // steps
    SelectGoalTypeView selectGoalTypeView; // step 1

    public SwapsView() {
        // one card is displayed at a time
        // proceed through each card step by step when creating a swap
        swapsCardLayout = new CardLayout();
        swapSteps = new JPanel(swapsCardLayout);

        // step 1
        selectGoalTypeView = new SelectGoalTypeView();
        swapSteps.add(selectGoalTypeView, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());

        JPanel createPreciseGoal = new JPanel();
        createPreciseGoal.add(new JLabel("Create Precise Goal"));
        swapSteps.add(createPreciseGoal, SwapWorkflowStep.PRECISE_GOAL_DETAILS.toString());

        JPanel createImpreciseGoal = new JPanel();
        createImpreciseGoal.add(new JLabel("Create Imprecise Goal"));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {DropdownOptionGoalIntensity.HIGH, DropdownOptionGoalIntensity.MEDIUM, DropdownOptionGoalIntensity.LOW};
        JComboBox intensityComboBox = new JComboBox<>(dropdownChoiceIntensityOptions);
        createImpreciseGoal.add(intensityComboBox);
        swapSteps.add(createImpreciseGoal, SwapWorkflowStep.IMPRECISE_GOAL_DETAILS.toString());

        /* TO DO: extract buttons into separate method */

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentStep++;
            handleStepChange();
        });
        nextButton.setPreferredSize(new Dimension(100, 30));

        // previous button
        previousButton = new JButton("Previous");
        previousButton.setEnabled(false); // cannot navigate back from initial step
        previousButton.addActionListener(e -> {
            currentStep--;
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
        previousButton.setEnabled(currentStep != FIRST_STEP);
        nextButton.setEnabled(currentStep != LAST_STEP);
        switch (currentStep) {
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
