package swaps.view;

import swaps.view.goals.DefineGoalsView;
import swaps.view.select_swap.SelectSwapView;
import swaps.view.swap_meal_details.SwapMealDetailsView;
import swaps.view.swap_statistics.SwapStatisticsView;

import javax.swing.*;
import java.awt.*;


public class SwapsView extends JPanel {
    private static final String NEXT_BUTTON_LABEL = "Next";
    private static final String PREVIOUS_BUTTON_LABEL = "Previous";

    private DefineGoalsView defineGoalsView;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton nextButton;
    private JButton previousButton;

    public SwapsView(DefineGoalsView defineGoalsView) {
        this.defineGoalsView = defineGoalsView;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initCardPanel();
        initNavigationButtons();
    }

    private void initCardPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(defineGoalsView, SwapsPresenter.DEFINE_GOALS_CARD_ID);
        cardPanel.add(new SelectSwapView(), SwapsPresenter.SELECT_SWAPS_CARD_ID);
        cardPanel.add(new SwapStatisticsView(), SwapsPresenter.SWAP_STATISTICS_CARD_ID);
        cardPanel.add(new SwapMealDetailsView(), SwapsPresenter.SWAP_MEAL_DETAILS_CARD_ID);
        cardLayout.show(cardPanel, SwapsPresenter.DEFINE_GOALS_CARD_ID);
        this.add(cardPanel);
    }

    private void initNavigationButtons() {
        JPanel buttonsSection = new JPanel();
        buttonsSection.setLayout(new FlowLayout());
        previousButton = new JButton(PREVIOUS_BUTTON_LABEL);
        buttonsSection.add(previousButton);
        nextButton = new JButton(NEXT_BUTTON_LABEL);
        buttonsSection.add(nextButton);
        this.add(buttonsSection);
    }

    public void showCard(String cardId) {
        cardLayout.show(cardPanel, cardId);
    }

    public void setPreviousButtonEnabled(boolean isEnabled) {
        previousButton.setEnabled(isEnabled);
    }

    public void setNextButtonEnabled(boolean isEnabled) {
        nextButton.setEnabled(isEnabled);
    }

    public void addPreviousButtonActionListener(Runnable listener) {
        previousButton.addActionListener(e -> listener.run());
    }

    public void addNextButtonActionListener(Runnable listener) {
        nextButton.addActionListener(e -> listener.run());
    }

    public DefineGoalsView getDefineGoalsView() {
        return defineGoalsView;
    }
}
