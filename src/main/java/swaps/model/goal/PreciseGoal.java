package swaps.model.goal;

import shared.Nutrient;
import shared.Unit;

public class PreciseGoal extends Goal {
    private final Unit unit;
    private final float amount;

    protected PreciseGoal(Nutrient nutrient, Direction direction, Unit unit, float amount) {
        super(nutrient, direction);
        this.unit = unit;
        this.amount = amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public float getAmount() {
        return amount;
    }
}
