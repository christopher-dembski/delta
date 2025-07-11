package swaps;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaps.view.*;


import static org.mockito.Mockito.*;

class TestSwapsPresenter {
    private SwapsPresenter presenter;
    private ISwapsView viewMock;

    @BeforeEach
    public void beforeEach() {
        viewMock = mock(ISwapsView.class);
        ISelectGoalTypeCard mockSelectGoalTypeCard = mock(ISelectGoalTypeCard.class);
        when(viewMock.getSelectGoalTypeCard()).thenReturn(mockSelectGoalTypeCard);
        ICreateImpreciseGoalCard mockCreateImpreciseGoalTypeCard = mock(ICreateImpreciseGoalCard.class);
        when(viewMock.getCreateImpreciseGoalCard()).thenReturn(mockCreateImpreciseGoalTypeCard);
        presenter = new SwapsPresenter(viewMock);
    }

    @Test
    public void testInitialState() {
        verify(viewMock.getSelectGoalTypeCard()).setSelectedGoalType(DropdownOptionGoalType.IMPRECISE);
        verify(viewMock.getSelectGoalTypeCard()).addGoalTypeDropDownListener(any());
        verify(viewMock.getCreateImpreciseGoalCard()).setSelectedGoalIntensity(DropdownOptionGoalIntensity.HIGH);
        verify(viewMock.getCreateImpreciseGoalCard()).addGoalIntensityDropdownListener(any());
    }

    @Test
    public void testChangeStepToCreateImpreciseGoal() {
        presenter.changeStep(2);
        verify(viewMock).setPreviousButtonEnabled(true);
        verify(viewMock).setNextButtonEnabled(false);
        verify(viewMock).setCard(SwapWorkflowStep.IMPRECISE_GOAL_DETAILS);
    }
}
