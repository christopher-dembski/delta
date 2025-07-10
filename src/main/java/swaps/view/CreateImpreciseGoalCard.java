package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class CreateImpreciseGoalCard extends JPanel {
    private JComboBox<DropdownOptionGoalIntensity> goalIntensityDropdown;

    protected CreateImpreciseGoalCard() {
        this.add(new JLabel("Create Imprecise Goal"));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {
                DropdownOptionGoalIntensity.HIGH,
                DropdownOptionGoalIntensity.MEDIUM,
                DropdownOptionGoalIntensity.LOW
        };
        goalIntensityDropdown = new JComboBox<>(dropdownChoiceIntensityOptions);
        this.add(goalIntensityDropdown);
    }

    public void addGoalIntensityDropdownListener(Consumer<DropdownOptionGoalIntensity> listener) {
        goalIntensityDropdown.addActionListener(e -> {
            DropdownOptionGoalIntensity selectedGoalIntensity =
                    (DropdownOptionGoalIntensity) goalIntensityDropdown.getSelectedItem();
            listener.accept(selectedGoalIntensity);
        });
    }
}
