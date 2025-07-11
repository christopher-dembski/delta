package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class FormFieldChooseOneOrTwoGoals extends JPanel {
    private JComboBox oneOrTwoGoalsDropdown;

    protected FormFieldChooseOneOrTwoGoals() {
        this.add(new JLabel("Specify a second goal?"));
        DropDownOptionCreateSecondGoal[] choices = {
                DropDownOptionCreateSecondGoal.NO,
                DropDownOptionCreateSecondGoal.YES
        };
        oneOrTwoGoalsDropdown = new JComboBox(choices);
        this.add(oneOrTwoGoalsDropdown);
    }

    public void setOneOrTwoGoalsDropdown(DropDownOptionCreateSecondGoal createSecondGoal) {
        oneOrTwoGoalsDropdown.setSelectedItem(createSecondGoal);
    }

    public void addOneOrTwoGoalsDropdownListener(Consumer<DropDownOptionCreateSecondGoal> listener) {
        oneOrTwoGoalsDropdown.addActionListener(e -> {
            DropDownOptionCreateSecondGoal yesNo = (DropDownOptionCreateSecondGoal) oneOrTwoGoalsDropdown.getSelectedItem();
            listener.accept(yesNo);
        });
    }
}
