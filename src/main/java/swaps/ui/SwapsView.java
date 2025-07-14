package swaps.ui;

import swaps.ui.goals.CreateGoalsView;
import swaps.ui.select_swap.SelectSwapView;
import swaps.ui.swap_meal_details.SwapMealDetailsView;
import swaps.ui.swap_statistics.SwapStatisticsView;

import javax.swing.*;
import java.awt.*;


public class SwapsView extends JPanel {
    private static final String NEXT_BUTTON_LABEL = "Next";
    private static final String PREVIOUS_BUTTON_LABEL = "Previous";

    private CreateGoalsView createGoalsView;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton nextButton;
    private JButton previousButton;

    public SwapsView(CreateGoalsView createGoalsView) {
        this.createGoalsView = createGoalsView;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initCardPanel();
        initNavigationButtons();
    }

    private void initCardPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createGoalsView, SwapsPresenter.DEFINE_GOALS_CARD_ID);
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

    public CreateGoalsView getDefineGoalsView() {
        return createGoalsView;
    }
}
