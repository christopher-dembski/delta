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
    protected SelectGoalTypeCard selectGoal1TypeCard;
    protected CreatePreciseGoalCard createPreciseGoal1Card;
    protected CreateImpreciseGoalCard createImpreciseGoal1Card;
    protected CreatePreciseGoalCard createPreciseGoal2Card;
    protected CreateImpreciseGoalCard createImpreciseGoal2Card;
    protected ChooseOneOrTwoGoalsCard chooseOneOrTwoGoalsCard;
    protected SelectGoalTypeCard selectGoal2TypeCard;

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
        selectGoal1TypeCard = new SelectGoalTypeCard();
        swapSteps.add(selectGoal1TypeCard, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());

        // Step 2a: create a precise goal
        createPreciseGoal1Card = new CreatePreciseGoalCard();
        swapSteps.add(createPreciseGoal1Card, SwapWorkflowStep.CREATE_GOAL_1_PRECISE.toString());

        // Step 2b: create an imprecise goal
        createImpreciseGoal1Card = new CreateImpreciseGoalCard();
        swapSteps.add(createImpreciseGoal1Card, SwapWorkflowStep.CREATE_GOAL_1_IMPRECISE.toString());

        // step 3: choose whether to create a second goal or not
        chooseOneOrTwoGoalsCard = new ChooseOneOrTwoGoalsCard();
        swapSteps.add(chooseOneOrTwoGoalsCard, SwapWorkflowStep.CHOOSE_ONE_OR_TWO_GOALS.toString());

        // step 4: choose whether to create a precise or imprecise second goal (optional)
        selectGoal2TypeCard = new SelectGoalTypeCard();
        swapSteps.add(selectGoal2TypeCard, SwapWorkflowStep.SELECT_GOAL_2_TYPE.toString());

        // Step 5a: create a precise goal (optional)
        createPreciseGoal2Card = new CreatePreciseGoalCard();
        swapSteps.add(createPreciseGoal2Card, SwapWorkflowStep.CREATE_GOAL_2_PRECISE.toString());

        // Step 5b: create an imprecise goal (optional)
        createImpreciseGoal2Card = new CreateImpreciseGoalCard();
        swapSteps.add(createImpreciseGoal2Card, SwapWorkflowStep.CREATE_GOAL_2_IMPRECISE.toString());

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
    public ISelectGoalTypeCard getSelectGoal1TypeCard() {
        return selectGoal1TypeCard;
    }

    @Override
    public ICreateImpreciseGoalCard getCreateImpreciseGoal1Card() {
        return createImpreciseGoal1Card;
    }

    @Override
    public ChooseOneOrTwoGoalsCard getChooseOneOrTwoGoalsCard() {
        return chooseOneOrTwoGoalsCard;
    }

    @Override
    public SelectGoalTypeCard getSelectGoal2TypeCard() {
        return selectGoal2TypeCard;
    }

    @Override
    public ICreateImpreciseGoalCard getCreateImpreciseGoal2Card() {
        return createImpreciseGoal2Card;
    }

    @Override
    public CreatePreciseGoalCard getCreatePreciseGoal2Card() {
        return createPreciseGoal2Card;
    }
}
