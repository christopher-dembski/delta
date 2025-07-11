package swaps.view;

import javax.swing.*;

public class SwapsPresenter {
    // static variables
    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 5;

    // instance variables
    private ISwapsView view;
    private int currentStep;
    private DropdownOptionGoalType selectedGoal1Type;
    private DropdownOptionGoalIntensity selectedGoal1Intensity;
    private DropDownCreateSecondGoal createSecondGoalYesNo;
    private DropdownOptionGoalType selectedGoal2Type;
    private DropdownOptionGoalIntensity selectedGoal2Intensity;

    public SwapsPresenter(ISwapsView view) {
        this.view = view;
        currentStep = FIRST_STEP;
        initDropDowns();
        initActionListeners();
    }

    private void initDropDowns() {
        // set state
        // call setters to ensure presenter and view agree on state
        selectedGoal1Type = DropdownOptionGoalType.IMPRECISE;
        view.getSelectGoal1TypeCard().setSelectedGoalType(selectedGoal1Type);
        selectedGoal1Intensity = DropdownOptionGoalIntensity.HIGH;
        view.getCreateImpreciseGoal1Card().setSelectedGoalIntensity(selectedGoal1Intensity);
        createSecondGoalYesNo = DropDownCreateSecondGoal.NO;
        view.getChooseOneOrTwoGoalsCard().setOneOrTwoGoalsDropdown(createSecondGoalYesNo);
        selectedGoal2Type = DropdownOptionGoalType.IMPRECISE;
        view.getSelectGoal2TypeCard().setSelectedGoalType(selectedGoal2Type);
        selectedGoal2Intensity = DropdownOptionGoalIntensity.HIGH;
        view.getCreateImpreciseGoal2Card().setSelectedGoalIntensity(selectedGoal2Intensity);
    }

    private void initActionListeners() {
        view.addNextButtonListener(e -> changeStep(currentStep + 1));
        view.addPreviousButtonListener(e -> changeStep(currentStep - 1));
        view.getSelectGoal1TypeCard().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal1Type = goalTypeFromDropDown;
        });
        view.getCreateImpreciseGoal1Card().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal1Intensity = goalIntensityFromDropdown;
        });
        view.getChooseOneOrTwoGoalsCard().addOneOrTwoGoalsDropdownListener(createSecondGoalYesNoFromDropdown -> {
            createSecondGoalYesNo = createSecondGoalYesNoFromDropdown;
        });
        view.getSelectGoal2TypeCard().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoal2Type = goalTypeFromDropDown;
        });
        view.getCreateImpreciseGoal2Card().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoal2Intensity = goalIntensityFromDropdown;
        });
    }

    public void changeStep(int newStep) {
        currentStep = newStep;
        view.setPreviousButtonEnabled(currentStep != SwapsPresenter.FIRST_STEP);
        view.setNextButtonEnabled(currentStep != SwapsPresenter.LAST_STEP);
        switch (currentStep) {
            case 1: {
                view.setCard(SwapWorkflowStep.SELECT_GOAL_TYPE);
                break;
            }
            case 2: {
                SwapWorkflowStep newPanelId = selectedGoal1Type.equals(DropdownOptionGoalType.PRECISE)
                        ? SwapWorkflowStep.CREATE_GOAL_1_PRECISE
                        : SwapWorkflowStep.CREATE_GOAL_1_IMPRECISE;
                view.setCard(newPanelId);
                break;
            }
            case 3: {
                view.setCard(SwapWorkflowStep.CHOOSE_ONE_OR_TWO_GOALS);
                break;
            }
            case 4: {
                if (createSecondGoalYesNo.equals(DropDownCreateSecondGoal.NO)) {
                    // skip creating second goal
                    changeStep(7);
                    break;
                }
                view.setCard(SwapWorkflowStep.SELECT_GOAL_2_TYPE);
                break;
            }
            case 5: {
                SwapWorkflowStep newCardId = selectedGoal2Type.equals(DropdownOptionGoalType.PRECISE)
                        ? SwapWorkflowStep.CREATE_GOAL_2_PRECISE
                        : SwapWorkflowStep.CREATE_GOAL_2_IMPRECISE;
                view.setCard(newCardId);
            }
        }
    }

    public static void main(String[] args) {
        // temporary main method for testing
        // this component would be rendered within the main page
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        SwapsView swapsView = new SwapsView();
        frame.add(swapsView);
        SwapsPresenter presenter = new SwapsPresenter(swapsView);
        frame.setVisible(true);
    }
}
