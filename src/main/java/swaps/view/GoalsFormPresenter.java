package swaps.view;


import javax.swing.*;

public class GoalsFormPresenter {
    // goals form
    private GoalsFormView goalsFormView;
    // for field values
    private DropdownOptionGoalType selectedGoal1Type;
    private DropdownOptionGoalIntensity selectedGoal1Intensity;
    private boolean createSecondGoal;
    private DropdownOptionGoalType selectedGoal2Type;
    private DropdownOptionGoalIntensity selectedGoal2Intensity;

    public GoalsFormPresenter(GoalsFormView goalsFormView) {
        this.goalsFormView = goalsFormView;
        initDropDowns();
        initActionListeners();
    }

    private void initDropDowns() {
        // call setters to ensure presenter and view agree on state
        selectedGoal1Type = DropdownOptionGoalType.IMPRECISE;
        goalsFormView.getGoal1TypeField().setSelectedGoalType(selectedGoal1Type);
        selectedGoal1Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsFormView.getGoal1IntensityField().setSelectedGoalIntensity(selectedGoal1Intensity);
        createSecondGoal = false;
        goalsFormView.getChooseOneOrTwoGoalsField().setOneOrTwoGoalsDropdown(createSecondGoal);
        selectedGoal2Type = DropdownOptionGoalType.IMPRECISE;
        goalsFormView.getGoal2TypeField().setSelectedGoalType(selectedGoal2Type);
        selectedGoal2Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsFormView.getGoal2IntensityField().setSelectedGoalIntensity(selectedGoal2Intensity);
    }

    private void initActionListeners() {
        goalsFormView.getGoal1TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal1Type = goalTypeFromDropDown;
        });
        goalsFormView.getGoal1IntensityField().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal1Intensity = goalIntensityFromDropdown;
        });
        goalsFormView.getChooseOneOrTwoGoalsField().addOneOrTwoGoalsDropdownListener(createSecondGoalFromCheckbox -> {
            goalsFormView.setGoal2SectionVisibility(createSecondGoalFromCheckbox);
        });
        goalsFormView.getGoal2TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal2Type = goalTypeFromDropDown;
        });
        goalsFormView.getGoal2IntensityField().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal2Intensity = goalIntensityFromDropdown;
        });
    }

    public static void main(String[] args) {
        // temporary main method for testing
        // this component would be rendered within the main page
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        GoalsFormView goalsFormView = new GoalsFormView();
        new GoalsFormPresenter(goalsFormView); // // registers event listeners
        frame.add(goalsFormView);
        frame.setVisible(true);
    }
}
