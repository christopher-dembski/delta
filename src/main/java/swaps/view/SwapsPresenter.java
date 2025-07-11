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

        // set state
        // call setters to ensure presenter and view agree on state
        selectedGoalType = DropdownOptionGoalType.IMPRECISE;
        view.getSelectGoalTypeCard().setSelectedGoalType(selectedGoalType);
        selectedGoalIntensity = DropdownOptionGoalIntensity.HIGH;
        view.getCreateImpreciseGoalCard().setSelectedGoalIntensity(selectedGoalIntensity);

        // add action listeners
        view.addNextButtonListener(e -> changeStep(currentStep + 1));
        view.addPreviousButtonListener(e -> changeStep(currentStep - 1));
        view.getSelectGoalTypeCard().addGoalTypeDropDownListener(goalTypeFromDropDown -> {
            selectedGoalType = goalTypeFromDropDown;
        });
        view.getCreateImpreciseGoalCard().addGoalIntensityDropdownListener(goalIntensityFromDropdown -> {
            selectedGoalIntensity = goalIntensityFromDropdown;
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
