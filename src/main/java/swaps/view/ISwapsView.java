package swaps.view;

import java.awt.event.ActionListener;

public interface ISwapsView {
    void setCard(SwapWorkflowStep workflowStep);

    void addNextButtonListener(ActionListener listener);

    void addPreviousButtonListener(ActionListener listener);

    void setNextButtonEnabled(boolean enabled);

    void setPreviousButtonEnabled(boolean enabled);

    ISelectGoalTypeCard getSelectGoal1TypeCard();

    ICreateImpreciseGoalCard getCreateImpreciseGoal1Card();

    ChooseOneOrTwoGoalsCard getChooseOneOrTwoGoalsCard();

    SelectGoalTypeCard getSelectGoal2TypeCard();

    ICreateImpreciseGoalCard getCreateImpreciseGoal2Card();

    CreatePreciseGoalCard getCreatePreciseGoal2Card();
}
