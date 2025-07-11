package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CreateImpreciseGoalCard extends JPanel implements ICreateImpreciseGoalCard {
    private JComboBox<DropdownOptionGoalIntensity> goalIntensityDropdown;

    protected CreateImpreciseGoalCard(int goalNumber) {
        // vertically stack components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Create Goal %d (Imprecise)".formatted(goalNumber)));
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

    @Override
    public void addGoalIntensityDropdownListener(Consumer<DropdownOptionGoalIntensity> listener) {
        goalIntensityDropdown.addActionListener(e -> {
            DropdownOptionGoalIntensity selectedGoalIntensity =
                    (DropdownOptionGoalIntensity) goalIntensityDropdown.getSelectedItem();
            listener.accept(selectedGoalIntensity);
        });
    }

    @Override
    public void setSelectedGoalIntensity(DropdownOptionGoalIntensity goalIntensity) {
        goalIntensityDropdown.setSelectedItem(goalIntensity);
    }
}
