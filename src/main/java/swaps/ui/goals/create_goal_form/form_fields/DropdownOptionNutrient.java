package swaps.ui.goals.create_goal_form.form_fields;

import meals.models.nutrient.Nutrient;

public class DropdownOptionNutrient {
    private final Nutrient nutrient;

    public DropdownOptionNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    @Override
    public String toString() {
        return nutrient.getNutrientName();
    }
}
