package profile.view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.ParseException;
import profile.model.Sex;
import profile.model.UnitSystem;

public class UserSignUp extends JFrame {
    private JPanel signUpPane;

    // Input fields
    private JTextField fullNameField;
    private JButton cancelButton;
    private JTextField ageField;
    private JComboBox<Sex> sexCombo;
    private JFormattedTextField dobField;

    // Numeric fields
    private JTextField heightField;
    private JTextField weightField;

    // Unit system dropdown
    private JComboBox<UnitSystem> unitSystemCombo;

    // Buttons
    private JButton createProfileButton;

    public UserSignUp() {
        setTitle("User Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize UI components FIRST
        initializeComponents();
        setContentPane(signUpPane);
        pack();
        setLocationRelativeTo(null);

        // Set up date formatter for DOB field (yyyy-MM-dd)
        try {
            if (dobField != null) {
                MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
                dateFormatter.setPlaceholderCharacter('_');
                dobField.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
            }
        } catch (ParseException e) {
            if (dobField != null) {
                dobField.setToolTipText("Please enter date in format: YYYY-MM-DD");
            }
        }
    }

    private void initializeComponents() {
        // initialize custom components that need special setup
        if (sexCombo == null) {
            sexCombo = new JComboBox<>();
            for (Sex sex : Sex.values()) {
                sexCombo.addItem(sex);
            }
        }

        if (unitSystemCombo == null) {
            unitSystemCombo = new JComboBox<>();
            for (UnitSystem unitSystem : UnitSystem.values()) {
                unitSystemCombo.addItem(unitSystem);
            }
        }

        // Set tooltip for DOB field to guide user input format
        if (dobField != null) {
            dobField.setToolTipText("Enter date in format: YYYY-MM-DD (e.g., 1990-12-25)");
        }
    }


}
