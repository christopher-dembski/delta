package swaps.ui.swap_statistics;

import javax.swing.*;
import java.awt.*;
import statistics.presenter.SwapComparisonPresenter;
import swaps.models.SwapWithMealContext;

/**
 * The view that renders statistics for meals before and after the swap.
 */
public class SwapStatisticsView extends JPanel {
    private SwapComparisonPresenter swapComparisonPresenter;
    private JPanel contentPanel;
    
    public SwapStatisticsView() {
        this.setLayout(new BorderLayout());
        this.swapComparisonPresenter = new SwapComparisonPresenter();
        
        // Create content panel for the comparison chart
        this.contentPanel = new JPanel(new BorderLayout());
        this.add(contentPanel, BorderLayout.CENTER);
        
        // Initial placeholder
        showPlaceholder();
    }
    
    /**
     * Updates the view to show comparison statistics for the selected swap.
     * @param selectedSwap The swap to analyze (oldFood vs newFood)
     */
    public void updateSwapComparison(SwapWithMealContext selectedSwap) {
        if (selectedSwap == null) {
            showPlaceholder();
            return;
        }
        
        try {
            // Create comparison visualization using our SwapComparisonPresenter
            JPanel comparisonPanel = swapComparisonPresenter.createSwapComparisonFromFoods(
                selectedSwap.oldFood(), 
                selectedSwap.newFood(),
                "Before Swap: " + selectedSwap.oldFood().getFoodDescription(),
                "After Swap: " + selectedSwap.newFood().getFoodDescription()
            );
            
            // Update the content
            contentPanel.removeAll();
            contentPanel.add(comparisonPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            
        } catch (Exception e) {
            showError("Error generating swap comparison: " + e.getMessage());
        }
    }
    
    /**
     * Shows a placeholder message when no swap is selected.
     */
    private void showPlaceholder() {
        contentPanel.removeAll();
        JLabel placeholderLabel = new JLabel("Select a swap to see nutritional comparison", JLabel.CENTER);
        placeholderLabel.setFont(placeholderLabel.getFont().deriveFont(Font.ITALIC, 14f));
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Shows an error message.
     */
    private void showError(String message) {
        contentPanel.removeAll();
        JLabel errorLabel = new JLabel(message, JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        contentPanel.add(errorLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
