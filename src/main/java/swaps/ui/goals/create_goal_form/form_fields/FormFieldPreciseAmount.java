package swaps.ui.goals.create_goal_form.form_fields;

import meals.models.nutrient.Nutrient;

import javax.swing.*;
import java.awt.*;

/**
 * Form field representing a precise amount.
 */
public class FormFieldPreciseAmount extends JPanel {
    private static final int PREFERRED_FORM_FIELD_WIDTH = 10;

    /**
     * @param nutrient The nutrient to select a precise amount for.
     */
    public FormFieldPreciseAmount(Nutrient nutrient) {
        super(new FlowLayout(FlowLayout.CENTER));
        JLabel formLabel = new JLabel("Enter an amount (%s)".formatted(nutrient.getNutrientUnit()));
        this.add(formLabel);
        this.add(new JTextField(PREFERRED_FORM_FIELD_WIDTH));
    }
}
