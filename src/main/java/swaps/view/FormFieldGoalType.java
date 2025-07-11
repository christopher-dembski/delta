package swaps.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FormFieldGoalType extends JPanel  {
    private JComboBox goalTypeDropDown;

    protected FormFieldGoalType() {
        // vertically stack components
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

    public void setSelectedGoalType(DropdownOptionGoalType goalIntensity) {
        goalTypeDropDown.setSelectedItem(goalIntensity);
    }

    public void addGoalTypeDropDownListener(Consumer<DropdownOptionGoalType> listener) {
        goalTypeDropDown.addActionListener(e -> {
            DropdownOptionGoalType selected = (DropdownOptionGoalType) goalTypeDropDown.getSelectedItem();
            listener.accept(selected);
        });
    }
}
