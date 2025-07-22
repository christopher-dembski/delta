package swaps.ui.select_swap;

import swaps.models.Swap;

import java.util.List;

public class SelectSwapPresenter {
    private Swap selectedSwap;

    public SelectSwapPresenter(SelectSwapView view, List<Swap> potentialSwaps) {
        view.addSelectionListener(swapListItem -> {
            if (swapListItem == null) return;
            selectedSwap = swapListItem.swap();
        });
        view.setPotentialSwaps(potentialSwaps);
    }

    public Swap getSelectedSwap() {
        return selectedSwap;
    }
}
