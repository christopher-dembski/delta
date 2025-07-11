package swaps.view;

import javax.swing.*;

public class SwapsPresenter {
    SwapsView swapsView;

    // goals form
    private GoalsFormView goalsForm;
    private DropdownOptionGoalType selectedGoal1Type;
    private DropdownOptionGoalIntensity selectedGoal1Intensity;
    private DropDownOptionCreateSecondGoal createSecondGoalYesNo;
    private DropdownOptionGoalType selectedGoal2Type;
    private DropdownOptionGoalIntensity selectedGoal2Intensity;

    public SwapsPresenter(SwapsView swapsView) {
        this.swapsView = swapsView;
        goalsForm = swapsView.getGoalsForm();
        initDropDowns();
        initActionListeners();
    }

    private void initDropDowns() {
        // call setters to ensure presenter and view agree on state
        selectedGoal1Type = DropdownOptionGoalType.IMPRECISE;
        goalsForm.getGoal1TypeField().setSelectedGoalType(selectedGoal1Type);
        selectedGoal1Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsForm.getGoal1IntensityField().setSelectedGoalIntensity(selectedGoal1Intensity);
        createSecondGoalYesNo = DropDownOptionCreateSecondGoal.NO;
        goalsForm.getChooseOneOrTwoGoalsField().setOneOrTwoGoalsDropdown(createSecondGoalYesNo);
        selectedGoal2Type = DropdownOptionGoalType.IMPRECISE;
        goalsForm.getGoal2TypeField().setSelectedGoalType(selectedGoal2Type);
        selectedGoal2Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsForm.getGoal2IntensityField().setSelectedGoalIntensity(selectedGoal2Intensity);
    }

    private void initActionListeners() {
        goalsForm.getGoal1TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal1Type = goalTypeFromDropDown;
        });
        goalsForm.getGoal1IntensityField().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal1Intensity = goalIntensityFromDropdown;
        });
        goalsForm.getChooseOneOrTwoGoalsField().addOneOrTwoGoalsDropdownListener(createSecondGoalYesNoFromDropdown -> {
            createSecondGoalYesNo = createSecondGoalYesNoFromDropdown;
        });
        goalsForm.getGoal2TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal2Type = goalTypeFromDropDown;
        });
        goalsForm.getGoal2IntensityField().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
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
        SwapsView swapsView = new SwapsView();
        new SwapsPresenter(swapsView); // registers event listeners
        frame.add(swapsView);
        frame.setVisible(true);
    }
}
