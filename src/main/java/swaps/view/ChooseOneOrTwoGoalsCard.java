package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class ChooseOneOrTwoGoalsCard extends JPanel {
    private JComboBox oneOrTwoGoalsDropdown;

    protected ChooseOneOrTwoGoalsCard() {
        this.add(new JLabel("Specify a second goal?"));
        DropDownCreateSecondGoal[] choices = {
                DropDownCreateSecondGoal.NO,
                DropDownCreateSecondGoal.YES
        };
        oneOrTwoGoalsDropdown = new JComboBox(choices);
        this.add(oneOrTwoGoalsDropdown);
    }

    public void setOneOrTwoGoalsDropdown(DropDownCreateSecondGoal createSecondGoal) {
        oneOrTwoGoalsDropdown.setSelectedItem(createSecondGoal);
    }

    public void addOneOrTwoGoalsDropdownListener(Consumer<DropDownCreateSecondGoal> listener) {
        oneOrTwoGoalsDropdown.addActionListener(e -> {
            DropDownCreateSecondGoal yesNo = (DropDownCreateSecondGoal) oneOrTwoGoalsDropdown.getSelectedItem();
            listener.accept(yesNo);
        });
    }
}
