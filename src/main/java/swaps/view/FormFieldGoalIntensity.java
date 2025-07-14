package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FormFieldGoalIntensity extends JPanel {
    private JComboBox<DropdownOptionGoalIntensity> goalIntensityDropdown;

    protected FormFieldGoalIntensity() {
        // vertically stack components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {
                DropdownOptionGoalIntensity.HIGH,
                DropdownOptionGoalIntensity.MEDIUM,
                DropdownOptionGoalIntensity.LOW
        };
        goalIntensityDropdown = new JComboBox<>(dropdownChoiceIntensityOptions);
        // wrap in JPanel with FlowLayout to avoid combo box filling entire window
        JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboWrapper.add(new JLabel("Select Goal Intensity:"));
        comboWrapper.add(goalIntensityDropdown);
        this.add(comboWrapper);
    }

    public void addListener(Consumer<DropdownOptionGoalIntensity> listener) {
        goalIntensityDropdown.addActionListener(e -> {
            DropdownOptionGoalIntensity selectedGoalIntensity =
                    (DropdownOptionGoalIntensity) goalIntensityDropdown.getSelectedItem();
            listener.accept(selectedGoalIntensity);
        });
    }

    public void setSelectedGoalIntensity(DropdownOptionGoalIntensity goalIntensity) {
        goalIntensityDropdown.setSelectedItem(goalIntensity);
    }
}
