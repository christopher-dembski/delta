package swaps.view;

import javax.swing.*;

public class CreateImpreciseGoalView extends JPanel {
    protected CreateImpreciseGoalView() {
        this.add(new JLabel("Create Imprecise Goal"));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {
                DropdownOptionGoalIntensity.HIGH,
                DropdownOptionGoalIntensity.MEDIUM,
                DropdownOptionGoalIntensity.LOW
        };
        this.add(new JComboBox<>(dropdownChoiceIntensityOptions));
    }
}
