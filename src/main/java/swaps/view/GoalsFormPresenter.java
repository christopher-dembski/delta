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
        initFormFieldsGoal1();
        initFormFieldOneOrTwoGoals();
        initFormFieldsGoal2();
        addActionListenersGoal1Fields();
        addActionListenersOneOrTwoGoalsField();
        addActionListenersGoal2Fields();
    }

    private void initFormFieldsGoal1() {
        selectedGoal1Type = DropdownOptionGoalType.IMPRECISE;
        goalsFormView.getGoal1TypeField().setSelectedGoalType(selectedGoal1Type);
        selectedGoal1Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsFormView.getGoal1IntensityField().setSelectedGoalIntensity(selectedGoal1Intensity);
        goalsFormView.setGoal1ImpreciseGoalFieldsVisibility(selectedGoal1Type.equals(DropdownOptionGoalType.IMPRECISE));
        goalsFormView.setGoal1PreciseGoalFieldsVisibility(selectedGoal1Type.equals(DropdownOptionGoalType.PRECISE));
    }

    private void initFormFieldOneOrTwoGoals() {
        createSecondGoal = false;
        goalsFormView.getChooseOneOrTwoGoalsField().setOneOrTwoGoalsDropdown(createSecondGoal);
    }

    private void initFormFieldsGoal2() {
        selectedGoal2Type = DropdownOptionGoalType.IMPRECISE;
        goalsFormView.getGoal2TypeField().setSelectedGoalType(selectedGoal2Type);
        selectedGoal2Intensity = DropdownOptionGoalIntensity.HIGH;
        goalsFormView.getGoal2IntensityField().setSelectedGoalIntensity(selectedGoal2Intensity);
        goalsFormView.setGoal2ImpreciseGoalFieldsVisibility(selectedGoal2Type.equals(DropdownOptionGoalType.IMPRECISE));
        goalsFormView.setGoal2PreciseGoalFieldsVisibility(selectedGoal2Type.equals(DropdownOptionGoalType.PRECISE));
    }

    private void addActionListenersGoal1Fields() {
        goalsFormView.getGoal1TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal1Type = goalTypeFromDropDown;
            if (selectedGoal1Type.equals(DropdownOptionGoalType.PRECISE)) {
                goalsFormView.setGoal1PreciseGoalFieldsVisibility(true);
                goalsFormView.setGoal1ImpreciseGoalFieldsVisibility(false);
            } else {  // IMPRECISE
                goalsFormView.setGoal1ImpreciseGoalFieldsVisibility(true);
                goalsFormView.setGoal1PreciseGoalFieldsVisibility(false);
            }
        });
        goalsFormView.getGoal1IntensityField().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal1Intensity = goalIntensityFromDropdown;
        });
    }

    private void addActionListenersOneOrTwoGoalsField() {
        goalsFormView.getChooseOneOrTwoGoalsField().addOneOrTwoGoalsDropdownListener(createSecondGoalFromCheckbox -> {
            createSecondGoal = createSecondGoalFromCheckbox;
            goalsFormView.setGoal2SectionVisibility(createSecondGoal);
        });
    }

    private void addActionListenersGoal2Fields() {
        goalsFormView.getGoal2TypeField().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal2Type = goalTypeFromDropDown;
            if (selectedGoal2Type.equals(DropdownOptionGoalType.PRECISE)) {
                goalsFormView.setGoal2PreciseGoalFieldsVisibility(true);
                goalsFormView.setGoal2ImpreciseGoalFieldsVisibility(false);
            } else {  // IMPRECISE
                goalsFormView.setGoal2ImpreciseGoalFieldsVisibility(true);
                goalsFormView.setGoal2PreciseGoalFieldsVisibility(false);
            }
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
