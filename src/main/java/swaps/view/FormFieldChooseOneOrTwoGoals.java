package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class FormFieldChooseOneOrTwoGoals extends JPanel {
    private JCheckBox oneOrTwoGoalsDropdown;

    protected FormFieldChooseOneOrTwoGoals() {
        this.add(new JLabel("Specify a second goal?"));
        oneOrTwoGoalsDropdown = new JCheckBox();
        this.add(oneOrTwoGoalsDropdown);
    }

    public void setOneOrTwoGoalsDropdown(boolean isSelected) {
        oneOrTwoGoalsDropdown.setSelected(isSelected);
    }

    public void addOneOrTwoGoalsDropdownListener(Consumer<Boolean> listener) {
        oneOrTwoGoalsDropdown.addActionListener(e -> {
            listener.accept(oneOrTwoGoalsDropdown.isSelected());
        });
    }
}
