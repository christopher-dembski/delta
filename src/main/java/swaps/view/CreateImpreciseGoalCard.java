package swaps.view;

import javax.swing.*;

public class CreateImpreciseGoalCard extends JPanel {
    protected CreateImpreciseGoalCard() {
        this.add(new JLabel("Create Imprecise Goal"));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {
                DropdownOptionGoalIntensity.HIGH,
                DropdownOptionGoalIntensity.MEDIUM,
                DropdownOptionGoalIntensity.LOW
        };
        this.add(new JComboBox<>(dropdownChoiceIntensityOptions));
    }
}
