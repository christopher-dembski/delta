package swaps.view;

import javax.swing.*;

public class SwapsPresenter {
    // static variables
    private static final int FIRST_STEP = 1;
    private static final int LAST_STEP = 2;

    // instance variables
    private SwapsView view;
    private int currentStep;

    public SwapsPresenter(SwapsView view) {
        this.view = view;
        currentStep = FIRST_STEP;
        view.addNextButtonListener(e -> {
            currentStep++;
            handleStepChange();
        });
        view.addPreviousButtonListener(e -> {
            currentStep--;
            handleStepChange();
        });
    }

    private void handleStepChange() {
        view.setPreviousButtonEnabled(currentStep != FIRST_STEP);
        view.setNextButtonEnabled(currentStep != LAST_STEP);
        switch (currentStep) {
            case 1: {
                view.swapsCardLayout.show(view.swapSteps, SwapWorkflowStep.SELECT_GOAL_TYPE.toString());
                break;
            }
            case 2: {
                String newPanelId = view.selectGoalTypeView.getSelectedGoalType().equals(DropdownOptionGoalType.PRECISE)
                        ? SwapWorkflowStep.PRECISE_GOAL_DETAILS.toString()
                        : SwapWorkflowStep.IMPRECISE_GOAL_DETAILS.toString();
                view.swapsCardLayout.show(view.swapSteps, newPanelId);
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
