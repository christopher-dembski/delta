package swaps.view;

import java.util.function.Consumer;

public interface ISelectGoalTypeCard {
    void setSelectedGoalType(DropdownOptionGoalType goalIntensity);
    void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener);
}
