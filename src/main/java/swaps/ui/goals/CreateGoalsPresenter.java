package swaps.ui.goals;

import swaps.ui.goals.create_goal_form.GoalsFormPresenter;

import javax.swing.*;


public class CreateGoalsPresenter {
    private CreateGoalsView createGoalsView;
    private GoalsFormPresenter goal1Presenter;
    private boolean createSecondGoal;
    private GoalsFormPresenter goal2Presenter;

    public CreateGoalsPresenter(
            CreateGoalsView createGoalsView,
            GoalsFormPresenter goal1Presenter,
            GoalsFormPresenter goal2Presenter
    ) {
        this.createGoalsView = createGoalsView;
        this.goal1Presenter = goal1Presenter;
        this.goal2Presenter = goal2Presenter;
        createSecondGoal = false;
        goal2Presenter.setFormVisibility(createSecondGoal);
        initDefineSecondGoalCheckbox();
        addActionListeners();
    }

    private void initDefineSecondGoalCheckbox() {
        createGoalsView.getDefineSecondGoalCheckbox().setOneOrTwoGoalsDropdown(createSecondGoal);
    }

    private void addActionListeners() {
        createGoalsView.getDefineSecondGoalCheckbox().addOneOrTwoGoalsDropdownListener(createSecondGoalFromCheckbox -> {
            createSecondGoal = createSecondGoalFromCheckbox;
            createGoalsView.setGoal2FormVisibility(createSecondGoal);
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
        CreateGoalsView createGoalsView = new CreateGoalsView();
        GoalsFormPresenter goal1Presenter = new GoalsFormPresenter(createGoalsView.getGoal1View());
        GoalsFormPresenter goal2Presenter = new GoalsFormPresenter(createGoalsView.getGoal2View());
        new CreateGoalsPresenter(createGoalsView, goal1Presenter, goal2Presenter);  // init state + register listeners
        frame.add(createGoalsView);
        // render
        frame.setVisible(true);
    }
}
