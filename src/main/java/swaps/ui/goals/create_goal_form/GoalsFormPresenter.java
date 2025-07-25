package swaps.ui.goals.create_goal_form;

import swaps.ui.goals.create_goal_form.form_fields.DropdownOptionGoalDirection;
import swaps.ui.goals.create_goal_form.form_fields.DropdownOptionGoalIntensity;
import swaps.ui.goals.create_goal_form.form_fields.DropdownOptionGoalType;

/**
 * The presenter controlling the state and conditional rendering of a goal creation form.
 */
public class GoalsFormPresenter {
    private GoalsFormView view;
    private DropdownOptionGoalType type;
    private DropdownOptionGoalIntensity intensity;
    private DropdownOptionGoalDirection direction;

    /**
     * @param view The goal creation form to manage.
     */
    public GoalsFormPresenter(GoalsFormView view) {
        this.view = view;
        initFormFields();
        addActionListeners();
    }

    /**
     * Initializes the form fields.
     * Helper method to be called in the constructor.
     */
    private void initFormFields() {
        type = DropdownOptionGoalType.IMPRECISE;
        view.getTypeField().setSelectedGoalType(type);
        intensity = DropdownOptionGoalIntensity.HIGH;
        view.getIntensityField().setSelectedGoalIntensity(intensity);
        direction = DropdownOptionGoalDirection.INCREASE;
        view.getDirectionField().setSelectedGoalDirection(direction);
        view.setIntensityFieldVisibility(type.equals(DropdownOptionGoalType.IMPRECISE));
        view.setPreciseAmountFieldVisibility(type.equals(DropdownOptionGoalType.PRECISE));
    }

    /**
     * Adds action listeners to specify what actions should be taken for each interactive element on the page.
     */
    private void addActionListeners() {
        view.getTypeField().addListener(typeFromDropdown -> {
            type = typeFromDropdown;
            if (type.equals(DropdownOptionGoalType.PRECISE)) {
                view.setPreciseAmountFieldVisibility(true);
                view.setIntensityFieldVisibility(false);
            } else if (type.equals(DropdownOptionGoalType.IMPRECISE)) {  // IMPRECISE
                view.setIntensityFieldVisibility(true);
                view.setPreciseAmountFieldVisibility(false);
            }
        });
        view.getIntensityField().addListener(intensityFromDropdown -> {
            intensity = intensityFromDropdown;
        });
        view.getDirectionField().addListener(directionFromDropdown -> {
            direction = directionFromDropdown;
        });
    }

    /**
     * Shows/hides the form.
     * @param isVisible Whether the form should be visible.
     */
    public void setFormVisibility(boolean isVisible) {
        view.setVisible(isVisible);
    }

    /**
     * @return The selected goal direction (increase/decrease).
     */
    public DropdownOptionGoalDirection getDirection() {
        return direction;
    }
}
