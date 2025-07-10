package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class SwapsView extends JPanel {
    // general layout components
    protected CardLayout swapsCardLayout;
    protected JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;

    // cards for each step in the workflow
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

        // next and previous buttons
        nextButton = buildNextButton();
        previousButton = buildPreviousButton();
        JPanel buttonContainer = new JPanel(new FlowLayout());
        buttonContainer.add(previousButton);
        buttonContainer.add(nextButton);

        // main layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(swapSteps);
        this.add(buttonContainer);
    }

    private JButton buildNextButton() {
        JButton button = new JButton("Next");
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private JButton buildPreviousButton() {
        JButton button = new JButton("Previous");
        button.setEnabled(false); // cannot navigate back from initial step
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    protected void addNextButtonListener(ActionListener listener) {
        nextButton.addActionListener(listener);
    }

    protected void addPreviousButtonListener(ActionListener listener) {
        previousButton.addActionListener(listener);
    }

    protected void setNextButtonEnabled(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    protected void setPreviousButtonEnabled(boolean enabled) {
        previousButton.setEnabled(enabled);
    }
}
