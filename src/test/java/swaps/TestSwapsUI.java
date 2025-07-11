package swaps;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import swaps.view.*;


import static org.mockito.Mockito.*;

class TestSwapsUI {
    private SwapsPresenter presenter;
    private ISwapsView view;

    @BeforeEach
    public void beforeEach() {
        view = spy(new SwapsView());
        presenter = new SwapsPresenter(view);
    }

    @Test
    public void testButtonStateFirstStep() {
        presenter.changeStep(1);
        verify(view).setNextButtonEnabled(true);
        verify(view).setPreviousButtonEnabled(false);
    }

    @Test
    public void testButtonStateMiddleStep() {
        presenter.changeStep(2);
        verify(view).setNextButtonEnabled(true);
        verify(view).setPreviousButtonEnabled(true);
    }

    @Test
    public void testButtonStateLastStep() {
        presenter.changeStep(5);
        verify(view).setNextButtonEnabled(false);
        verify(view).setPreviousButtonEnabled(true);
    }

    @Test
    public void testNavigateGoal1WithImpreciseGoal() {
        view.getSelectGoal1TypeCard().setSelectedGoalType(DropdownOptionGoalType.IMPRECISE);
        presenter.changeStep(2);
        verify(view).setCard(SwapWorkflowStep.CREATE_GOAL_1_IMPRECISE);
    }

    @Test
    public void testNavigateGoal1WithPreciseGoal() {
        view.getSelectGoal1TypeCard().setSelectedGoalType(DropdownOptionGoalType.PRECISE);
        presenter.changeStep(2);
        verify(view).setCard(SwapWorkflowStep.CREATE_GOAL_1_PRECISE);
    }

    @Test
    public void testNavigateToChooseOneOrTwoGoals() {
        presenter.changeStep(3);
        verify(view).setCard(SwapWorkflowStep.CHOOSE_ONE_OR_TWO_GOALS);
    }

    @Test
    public void testNavigateGoal2TypeCardWhenTwoGoalsAreSelected() {
        view.getChooseOneOrTwoGoalsCard().setOneOrTwoGoalsDropdown(DropDownCreateSecondGoal.YES);
        presenter.changeStep(4);
        verify(view).setCard(SwapWorkflowStep.SELECT_GOAL_2_TYPE);
    }

    @Disabled("Do not have this step implemented yet.")
    @Test
    public void testSkipGoal2CreationWhenOnlyOneGoalIsSelected() {
        view.getChooseOneOrTwoGoalsCard().setOneOrTwoGoalsDropdown(DropDownCreateSecondGoal.NO);
        presenter.changeStep(4);
        // TO DO: add assertion
    }

    @Test
    public void testNavigateGoal2WithPreciseGoal() {
        view.getChooseOneOrTwoGoalsCard().setOneOrTwoGoalsDropdown(DropDownCreateSecondGoal.YES);
        view.getSelectGoal2TypeCard().setSelectedGoalType(DropdownOptionGoalType.PRECISE);
        presenter.changeStep(5);
        verify(view).setCard(SwapWorkflowStep.CREATE_GOAL_2_PRECISE);
    }

    @Test
    public void testNavigateGoal2WithImpreciseGoal() {
        view.getChooseOneOrTwoGoalsCard().setOneOrTwoGoalsDropdown(DropDownCreateSecondGoal.YES);
        view.getSelectGoal2TypeCard().setSelectedGoalType(DropdownOptionGoalType.IMPRECISE);
        presenter.changeStep(5);
        verify(view).setCard(SwapWorkflowStep.CREATE_GOAL_2_IMPRECISE);
    }
}
