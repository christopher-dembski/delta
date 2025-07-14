package swaps.view;


import swaps.view.goals.DefineGoalsPresenter;
import swaps.view.goals.DefineGoalsView;
import swaps.view.goals.form.GoalsFormPresenter;

import javax.swing.*;
import java.lang.reflect.Array;

public class SwapsPresenter {
    protected static final String DEFINE_GOALS_CARD_ID = "DEFINE_GOALS";
    protected static final String SELECT_SWAPS_CARD_ID = "SELECT_SWAPS";
    protected static final String SWAP_STATISTICS_CARD_ID = "SWAP_STATISTICS";
    protected static final String SWAP_MEAL_DETAILS_CARD_ID = "SWAP_MEAL_DETAILS";
    protected static final String[] CARD_IDS = {
            DEFINE_GOALS_CARD_ID,
            SELECT_SWAPS_CARD_ID,
            SWAP_STATISTICS_CARD_ID,
            SWAP_MEAL_DETAILS_CARD_ID
    };

    private final SwapsView view;
    private int currentCardIndex;
    private final int lastCardIndex;

    public SwapsPresenter(SwapsView view) {
        this.view = view;
        currentCardIndex = 0;
        lastCardIndex = Array.getLength(CARD_IDS) - 1;
        initDefineGoalsView();
        addPreviousButtonActionListener();
        view.setPreviousButtonEnabled(false);
        addNextButtonActionListener();
        view.setNextButtonEnabled(true);
    }

    private void initDefineGoalsView() {
        DefineGoalsView defineGoalsView = view.getDefineGoalsView();
        GoalsFormPresenter goal1Presenter = new GoalsFormPresenter(defineGoalsView.getGoal1View());
        GoalsFormPresenter goal2Presenter = new GoalsFormPresenter(defineGoalsView.getGoal2View());
        new DefineGoalsPresenter(defineGoalsView, goal1Presenter, goal2Presenter);
    }

    private void addPreviousButtonActionListener() {
        view.addPreviousButtonActionListener(() -> {
            if (currentCardIndex == 0) return;
            currentCardIndex--;
            updateVisibleCardAndNavigationControls();
        });
    }

    private void addNextButtonActionListener() {
        view.addNextButtonActionListener(() -> {
            if (currentCardIndex == lastCardIndex) return;
            currentCardIndex++;
            updateVisibleCardAndNavigationControls();
        });
    }

    private void updateVisibleCardAndNavigationControls() {
        view.setPreviousButtonEnabled(currentCardIndex > 0);
        view.setNextButtonEnabled(currentCardIndex < lastCardIndex);
        view.showCard(CARD_IDS[currentCardIndex]);
    }

    public static void main(String[] args) {
        // this is a temporary method for testing
        // eventually, this view will become one tab in the main UI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        // set up view and presenter
        SwapsView swapsView = new SwapsView(new DefineGoalsView());
        new SwapsPresenter(swapsView);
        frame.add(swapsView);
        // render
        frame.setVisible(true);
    }
}
