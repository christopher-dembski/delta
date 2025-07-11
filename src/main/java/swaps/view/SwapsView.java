package swaps.view;

import javax.swing.*;


public class SwapsView extends JPanel {
    private GoalsFormView goalsFormView;

    public SwapsView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        goalsFormView = new GoalsFormView();
        this.add(goalsFormView);
    }

    public GoalsFormView getGoalsForm() {
        return goalsFormView;
    }
}
