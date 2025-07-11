package swaps.view;

import javax.swing.*;
import java.util.function.Consumer;

public class CreateImpreciseGoalCard extends JPanel implements ICreateImpreciseGoalCard {
    private JComboBox<DropdownOptionGoalIntensity> goalIntensityDropdown;

    protected CreateImpreciseGoalCard(int goalNumber) {
        this.add(new JLabel("Create Goal %d (Imprecise)".formatted(goalNumber)));
        DropdownOptionGoalIntensity[] dropdownChoiceIntensityOptions = {
                DropdownOptionGoalIntensity.HIGH,
                DropdownOptionGoalIntensity.MEDIUM,
                DropdownOptionGoalIntensity.LOW
        };
        goalIntensityDropdown = new JComboBox<>(dropdownChoiceIntensityOptions);
        this.add(goalIntensityDropdown);
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
