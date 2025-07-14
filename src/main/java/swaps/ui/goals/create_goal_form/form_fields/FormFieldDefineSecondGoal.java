package swaps.ui.goals.create_goal_form.form_fields;

import javax.swing.*;
import java.util.function.Consumer;

public class FormFieldDefineSecondGoal extends JPanel {
    private JCheckBox oneOrTwoGoalsDropdown;

    public FormFieldDefineSecondGoal() {
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
