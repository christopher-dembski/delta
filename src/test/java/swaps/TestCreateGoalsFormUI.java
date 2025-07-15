package swaps;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaps.ui.goals.create_goal_form.GoalsFormPresenter;
import swaps.ui.goals.create_goal_form.GoalsFormView;
import swaps.ui.goals.create_goal_form.form_fields.DropdownOptionGoalType;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TestCreateGoalsFormUI {
    private GoalsFormView view;

    @BeforeEach
    public void beforeEach() {
        view = spy(new GoalsFormView("Goal"));
        new GoalsFormPresenter(view);
    }

    @Test
    public void testImpreciseFieldsShownOnInitialRender() {
        verify(view).setIntensityFieldVisibility(true);
    }

    @Test
    public void testPreciseFieldsHiddenOnInitialRender() {
        verify(view).setPreciseAmountFieldVisibility(false);
    }

    @Test
    public void testPreciseFieldsShownAfterUserSelectsPreciseGoalType() {
        view.getTypeField().setSelectedGoalType(DropdownOptionGoalType.PRECISE);
        verify(view).setIntensityFieldVisibility(false);
        verify(view).setPreciseAmountFieldVisibility(true);
    }
}
