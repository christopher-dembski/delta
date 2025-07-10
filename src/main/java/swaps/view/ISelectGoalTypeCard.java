package swaps.view;

import java.util.function.Consumer;

public interface ISelectGoalTypeCard {
    void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener);
}
