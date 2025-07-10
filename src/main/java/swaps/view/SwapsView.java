package swaps.view;

import javax.swing.*;
import java.awt.*;


public class SwapsView extends JPanel {
    // constants
    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    // application state
    private int currentStep = 1;

    // general layout components
    protected CardLayout swapsCardLayout;
    protected JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;
    // steps
    protected final SelectGoalTypeCard selectGoalTypeView;
    protected final CreatePreciseGoalCard createPreciseGoalCard;
    protected final CreateImpreciseGoalCard createImpreciseGoal;

    public SwapsView() {
        // one card is displayed at a time
        // proceed through each card step by step when creating a swap
        swapsCardLayout = new CardLayout();
        swapSteps = new JPanel(swapsCardLayout);

        // Step 1: select a goal type (precise or imprecise)
        selectGoalTypeView = new SelectGoalTypeCard();
        swapSteps.add(selectGoalTypeView, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());

        // Step 2a: create a precise goal
        createPreciseGoalCard = new CreatePreciseGoalCard();
        swapSteps.add(createPreciseGoalCard, SwapWorkflowStep.PRECISE_GOAL_DETAILS.toString());

        // Step 2b: create an imprecise goal
        createImpreciseGoal = new CreateImpreciseGoalCard();
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
                String newPanelId = selectGoalTypeView.getSelectedGoalType().equals(DropdownOptionGoalType.PRECISE)
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
