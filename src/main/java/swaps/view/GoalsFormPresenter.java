package swaps.view;

public class GoalsFormPresenter {
    private GoalsFormView view;

    private DropdownOptionGoalType type;
    private DropdownOptionGoalIntensity intensity;

    public GoalsFormPresenter(GoalsFormView view) {
        this.view = view;
        initFormFields();
        addActionListeners();
    }

    private void initFormFields() {
        type = DropdownOptionGoalType.IMPRECISE;
        view.getTypeField().setSelectedGoalType(type);
        intensity = DropdownOptionGoalIntensity.HIGH;
        view.getIntensityField().setSelectedGoalIntensity(intensity);
        view.setIntensityFieldVisibility(type.equals(DropdownOptionGoalType.IMPRECISE));
        view.setPreciseAmountFieldVisibility(type.equals(DropdownOptionGoalType.PRECISE));
    }

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
    }

    public void setFormVisibility(boolean isVisible) {
        view.setVisible(isVisible);
    }
}
