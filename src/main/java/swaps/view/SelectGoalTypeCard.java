package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SelectGoalTypeCard extends JPanel implements ISelectGoalTypeCard {
    private JComboBox goalTypeDropDown;

    protected SelectGoalTypeCard(int goalNumber) {
        // vertically stack components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Select Goal Type %d".formatted(goalNumber)));
        DropdownOptionGoalType[] choices = {
                DropdownOptionGoalType.PRECISE,
                DropdownOptionGoalType.IMPRECISE
        };
        goalTypeDropDown = new JComboBox<>(choices);
        // wrap in JPanel with FlowLayout to avoid combo box filling entire window
        JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboWrapper.add(new JLabel("Select Goal Type:"));
        comboWrapper.add(goalTypeDropDown);
        this.add(comboWrapper);
    }

    @Override
    public void setSelectedGoalType(DropdownOptionGoalType goalIntensity) {
        goalTypeDropDown.setSelectedItem(goalIntensity);
    }

    @Override
    public void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener) {
        goalTypeDropDown.addActionListener(e -> {
            DropdownOptionGoalType selected = (DropdownOptionGoalType) goalTypeDropDown.getSelectedItem();
            listener.accept(selected);
        });
    }
}
