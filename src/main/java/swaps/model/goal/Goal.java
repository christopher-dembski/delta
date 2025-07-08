package swaps.model.goal;

import shared.Nutrient;

public abstract class Goal {
    private final Nutrient nutrient;
    private final Direction direction;

    protected Goal(Nutrient nutrient, Direction direction) {
        this.nutrient = nutrient;
        this.direction = direction;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public Direction getDirection() {
        return direction;
    }
}
