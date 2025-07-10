package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class SelectGoalTypeCard extends JPanel {
    private JComboBox goalTypeDropDown;

    protected SelectGoalTypeCard() {
        this.add(new JLabel("Select Goal Type"));
        DropdownOptionGoalType[] choices = {
                DropdownOptionGoalType.PRECISE,
                DropdownOptionGoalType.IMPRECISE
        };
        goalTypeDropDown = new JComboBox<>(choices);
        this.add(goalTypeDropDown);
    }

    public void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener) {
        goalTypeDropDown.addActionListener(e -> {
            DropdownOptionGoalType selected = (DropdownOptionGoalType) goalTypeDropDown.getSelectedItem();
            listener.accept(selected);
        });
    }
}
