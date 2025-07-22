package swaps.ui.select_swap;


import swaps.models.Swap;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * View for selecting a swap from the list of options.
 */
public class SelectSwapView extends JPanel {
    private static final String TITLE = "Select Swap From List";

    private final DefaultListModel<SwapListItem> potentialSwapListModel;
    private final JList<SwapListItem> potentialSwapList;

    public SelectSwapView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(TITLE));
        JPanel swapsContainer = new JPanel();
        potentialSwapListModel = new DefaultListModel<>();
        potentialSwapList = new JList<>(potentialSwapListModel);
        swapsContainer.add(potentialSwapList);
        this.add(swapsContainer);
    }

    protected void setPotentialSwaps(List<Swap> potentialSwaps) {
        for (Swap swap: potentialSwaps) {
            potentialSwapListModel.addElement(new SwapListItem(swap));
        }
    }

    protected void addSelectionListener(Consumer<SwapListItem> listener) {
        potentialSwapList.addListSelectionListener((e) -> {
            listener.accept(potentialSwapList.getSelectedValue());
        });
    }
}
