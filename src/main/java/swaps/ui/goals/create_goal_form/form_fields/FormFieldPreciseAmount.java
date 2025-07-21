package swaps.ui.goals.create_goal_form.form_fields;

import meals.models.nutrient.Nutrient;

import javax.swing.*;
import java.awt.*;

/**
 * Form field representing a precise amount.
 */
public class FormFieldPreciseAmount extends JPanel {
    public FormFieldPreciseAmount(Nutrient nutrient) {
        JPanel formField = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel formLabel = new JLabel("Enter an amount (%s)".formatted(nutrient.getNutrientUnit()));
        formField.add(formLabel);
        formField.add(new JTextField(10));
        this.add(formField);
    }
}
