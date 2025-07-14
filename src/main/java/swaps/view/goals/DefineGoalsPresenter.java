package swaps.view.goals;

import swaps.view.goals.form.GoalsFormPresenter;

import javax.swing.*;


public class DefineGoalsPresenter {
    private DefineGoalsView defineGoalsView;
    private GoalsFormPresenter goal1Presenter;
    private boolean createSecondGoal;
    private GoalsFormPresenter goal2Presenter;

    public DefineGoalsPresenter(
            DefineGoalsView defineGoalsView,
            GoalsFormPresenter goal1Presenter,
            GoalsFormPresenter goal2Presenter
    ) {
        this.defineGoalsView = defineGoalsView;
        this.goal1Presenter = goal1Presenter;
        this.goal2Presenter = goal2Presenter;
        createSecondGoal = false;
        goal2Presenter.setFormVisibility(createSecondGoal);
        initDefineSecondGoalCheckbox();
        addActionListeners();
    }

    private void initDefineSecondGoalCheckbox() {
        defineGoalsView.getDefineSecondGoalCheckbox().setOneOrTwoGoalsDropdown(createSecondGoal);
    }

    private void addActionListeners() {
        defineGoalsView.getDefineSecondGoalCheckbox().addOneOrTwoGoalsDropdownListener(createSecondGoalFromCheckbox -> {
            createSecondGoal = createSecondGoalFromCheckbox;
            defineGoalsView.setGoal2FormVisibility(createSecondGoal);
        });
    }

    public static void main(String[] args) {
        // temporary method for testing
        // demonstrating the use of the goals form
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        // setup views and presenters
        DefineGoalsView defineGoalsView = new DefineGoalsView();
        GoalsFormPresenter goal1Presenter = new GoalsFormPresenter(defineGoalsView.getGoal1View());
        GoalsFormPresenter goal2Presenter = new GoalsFormPresenter(defineGoalsView.getGoal2View());
        new DefineGoalsPresenter(defineGoalsView, goal1Presenter, goal2Presenter);  // init state + register listeners
        frame.add(defineGoalsView);
        // render
        frame.setVisible(true);
    }
}
