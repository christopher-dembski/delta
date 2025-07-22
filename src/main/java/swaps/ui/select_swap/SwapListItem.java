package swaps.ui.select_swap;

import swaps.models.Swap;

public record SwapListItem(Swap swap) {

    @Override
    public String toString() {
        String oldFoodName = swap.oldFood().getFoodDescription();
        String newFoodName = swap.newFood().getFoodDescription();
        return "%s → %s".formatted(oldFoodName, newFoodName);
    }
}
