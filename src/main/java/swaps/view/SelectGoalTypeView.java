package swaps.view;

import javax.swing.*;

public class SelectGoalTypeView extends JPanel {
    private DropdownOptionGoalType selectedGoalType;

    protected SelectGoalTypeView() {
        this.add(new JLabel("Select Goal Type"));
        DropdownOptionGoalType[] choices = {DropdownOptionGoalType.PRECISE, DropdownOptionGoalType.IMPRECISE};
        selectedGoalType = choices[0]; // combo box defaults to selecting first element
        JComboBox goalTypeChoices = new JComboBox<>(choices);
        goalTypeChoices.addActionListener(e -> selectedGoalType = (DropdownOptionGoalType) goalTypeChoices.getSelectedItem());
        this.add(goalTypeChoices);
    }

    protected DropdownOptionGoalType getSelectedGoalType() {
        return selectedGoalType;
    }
}
