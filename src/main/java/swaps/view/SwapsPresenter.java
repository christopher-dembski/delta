package swaps.view;

import javax.swing.*;

public class SwapsPresenter {
    // static variables
    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    // instance variables
    private ISwapsView view;
    private int currentStep;
    private DropdownOptionGoalType selectedGoalType;
    private DropdownOptionGoalIntensity selectedGoalIntensity;

    public SwapsPresenter(ISwapsView view) {
        // basic initialization
        this.view = view;
        currentStep = FIRST_STEP;
        selectedGoalType = DropdownOptionGoalType.IMPRECISE;
        view.getSelectGoalTypeCard().setSelectedGoalType(selectedGoalType);  // ensure presenter and view agree

        // add action listeners
        view.addNextButtonListener(e -> {
            currentStep++;
            handleStepChange();
        });
        view.addPreviousButtonListener(e -> {
            currentStep--;
            handleStepChange();
        });
        view.getSelectGoalTypeCard().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoalType = goalTypeFromDropDown;
        });
        view.getCreateImpreciseGoalCard().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoalIntensity = goalIntensityFromDropdown;
        });
    }

    private void handleStepChange() {
        view.setPreviousButtonEnabled(currentStep != FIRST_STEP);
        view.setNextButtonEnabled(currentStep != LAST_STEP);
        switch (currentStep) {
            case 1: {
                view.setCard(SwapWorkflowStep.SELECT_GOAL_TYPE);
                break;
            }
            case 2: {
                SwapWorkflowStep newPanelId = selectedGoalType.equals(DropdownOptionGoalType.PRECISE)
                        ? SwapWorkflowStep.PRECISE_GOAL_DETAILS
                        : SwapWorkflowStep.IMPRECISE_GOAL_DETAILS;
                view.setCard(newPanelId);
                break;
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
