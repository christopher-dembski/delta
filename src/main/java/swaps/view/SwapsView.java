package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class SwapsView extends JPanel implements ISwapsView {
    // general layout components
    private CardLayout swapsCardLayout;
    private JPanel swapSteps;
    private JButton nextButton;
    private JButton previousButton;
    private JPanel buttonContainer;

    // cards for each step in the workflow
    protected SelectGoalTypeCard selectGoalTypeView;
    protected CreatePreciseGoalCard createPreciseGoalCard;
    protected CreateImpreciseGoalCard createImpreciseGoalCard;

    public SwapsView() {
        initCards();
        initButtons();
        initLayout();
    }

    private void initCards() {
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
        createImpreciseGoalCard = new CreateImpreciseGoalCard();
        swapSteps.add(createImpreciseGoalCard, SwapWorkflowStep.IMPRECISE_GOAL_DETAILS.toString());
    }

    private void initLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(swapSteps);
        this.add(buttonContainer);
    }

    private void initButtons() {
        // next and previous buttons
        nextButton = buildNextButton();
        previousButton = buildPreviousButton();
        buttonContainer = new JPanel(new FlowLayout());
        buttonContainer.add(previousButton);
        buttonContainer.add(nextButton);
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

    @Override
    public void setCard(SwapWorkflowStep workflowStep) {
        swapsCardLayout.show(swapSteps, workflowStep.toString());
    }

    @Override
    public void addNextButtonListener(ActionListener listener) {
        nextButton.addActionListener(listener);
    }

    @Override
    public void addPreviousButtonListener(ActionListener listener) {
        previousButton.addActionListener(listener);
    }

    @Override
    public void setNextButtonEnabled(boolean enabled) {
        nextButton.setEnabled(enabled);
    }

    @Override
    public void setPreviousButtonEnabled(boolean enabled) {
        previousButton.setEnabled(enabled);
    }

    @Override
    public ISelectGoalTypeCard getSelectGoalTypeCard() {
        return selectGoalTypeView;
    }

    @Override
    public ICreateImpreciseGoalCard getCreateImpreciseGoalCard() {
        return createImpreciseGoalCard;
    }
}
