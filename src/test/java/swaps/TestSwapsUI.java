package swaps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaps.ui.SwapsPresenter;
import swaps.ui.SwapsView;
import swaps.ui.goals.CreateGoalsView;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TestSwapsUI {
    private SwapsView view;

    @BeforeEach
    public void beforeEach() {
        view = spy(new SwapsView(new CreateGoalsView()));
        new SwapsPresenter(view);  // initialize state and add action listeners to view
    }

    @Test
    public void testDisablesPreviousButtonAndEnablesNextButtonOnFirstCard() {
        verify(view).setPreviousButtonEnabled(false);
        verify(view).setNextButtonEnabled(true);
    }

    @Test
    public void testEnablesPreviousButtonAndEnablesNextButtonOnMiddleCard() {
        verify(view).setPreviousButtonEnabled(false);
        verify(view).setNextButtonEnabled(true);
    }
}
