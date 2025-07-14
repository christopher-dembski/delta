package swaps.ui.goals.create_goal_form;

import swaps.ui.goals.create_goal_form.form_fields.FormFieldGoalIntensity;
import swaps.ui.goals.create_goal_form.form_fields.FormFieldGoalType;
import swaps.ui.goals.create_goal_form.form_fields.FormFieldPreciseAmount;

import javax.swing.*;
import java.awt.*;

public class GoalsFormView extends JPanel {
    private FormFieldGoalType typeField;
    private FormFieldPreciseAmount preciseAmountField;
    private FormFieldGoalIntensity intensityField;

    public GoalsFormView(String header) {
        initLayout(header);
        initFormFields();
    }

    private void initLayout(String header) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(new JLabel(header));
    }

    private void initFormFields() {
        typeField = new FormFieldGoalType();
        this.add(typeField);
        preciseAmountField = new FormFieldPreciseAmount();
        this.add(preciseAmountField);
        intensityField = new FormFieldGoalIntensity();
        this.add(intensityField);
    }

    /* Conditional Rendering */

    public void setPreciseAmountFieldVisibility(boolean isVisible) {
        preciseAmountField.setVisible(isVisible);
    }

    public void setIntensityFieldVisibility(boolean isVisible) {
        intensityField.setVisible(isVisible);
    }

    /* Form Fields */

    public FormFieldGoalType getTypeField() {
        return typeField;
    }

    public FormFieldGoalIntensity getIntensityField() {
        return intensityField;
    }
}
