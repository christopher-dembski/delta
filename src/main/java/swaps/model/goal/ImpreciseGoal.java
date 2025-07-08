package swaps.model.goal;

import shared.Nutrient;

public class ImpreciseGoal extends Goal {
    private final Intensity intensity;

    public ImpreciseGoal(Nutrient nutrient, Direction direction, Intensity intensity) {
        super(nutrient, direction);
        this.intensity = intensity;
    }

    public Intensity getIntensity() {
        return intensity;
    }
}
