package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class SelectGoalTypeCard extends JPanel implements ISelectGoalTypeCard {
    private JComboBox goalTypeDropDown;

    protected SelectGoalTypeCard(int goalNumber) {
        this.add(new JLabel("Select Goal Type %d".formatted(goalNumber)));
        DropdownOptionGoalType[] choices = {
                DropdownOptionGoalType.PRECISE,
                DropdownOptionGoalType.IMPRECISE
        };
        goalTypeDropDown = new JComboBox<>(choices);
        this.add(goalTypeDropDown);
    }

    @Override
    public void setSelectedGoalType(DropdownOptionGoalType goalIntensity) {
        goalTypeDropDown.setSelectedItem(goalIntensity);
    }

    @Override
    public void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener) {
        goalTypeDropDown.addActionListener(e -> {
            DropdownOptionGoalType selected = (DropdownOptionGoalType) goalTypeDropDown.getSelectedItem();
            listener.accept(selected);
        });
    }
}
