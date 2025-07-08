package swaps.model.swap;

import shared.Ingredient;

public class Swap {
    private final Ingredient oldIngredient;
    private final Ingredient newIngredient;

    public Swap(Ingredient oldIngredient, Ingredient newIngredient) {
        this.oldIngredient = oldIngredient;
        this.newIngredient = newIngredient;
    }

    public Ingredient getOldIngredient() {
        return oldIngredient;
    }

    public Ingredient getNewIngredient() {
        return newIngredient;
    }
}
