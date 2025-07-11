package swaps;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaps.view.*;


import static org.mockito.Mockito.*;

class TestSwapsPresenter {
    private SwapsPresenter presenter;
    private ISwapsView view;

    @BeforeEach
    public void beforeEach() {
        view = spy(new SwapsView());
        presenter = new SwapsPresenter(view);
    }

    @Test
    public void testButtonState() {
        presenter.changeStep(2);
        verify(view).setNextButtonEnabled(false);
        verify(view).setPreviousButtonEnabled(true);
        presenter.changeStep(1);
        verify(view).setNextButtonEnabled(true);
        verify(view).setPreviousButtonEnabled(false);
    }

    @Test
    public void testNavigateToCreateImpreciseGoalCard() {
        presenter.changeStep(2);
        verify(view).setCard(SwapWorkflowStep.IMPRECISE_GOAL_DETAILS);
    }

    @Test
    public void testNavigateToPreciseGoalCard() {
        view.getSelectGoalTypeCard().setSelectedGoalType(DropdownOptionGoalType.PRECISE);
        presenter.changeStep(2);
        verify(view).setCard(SwapWorkflowStep.PRECISE_GOAL_DETAILS);
    }
}
