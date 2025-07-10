package swaps.view;

import java.util.function.Consumer;

public interface ICreateImpreciseGoalCard {
    void addGoalIntensityDropdownListener(Consumer<DropdownOptionGoalIntensity> listener);

    void setSelectedGoalIntensity(DropdownOptionGoalIntensity goalIntensity);
}
