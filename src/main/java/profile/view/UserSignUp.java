package profile.view;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

import profile.model.Sex;
import profile.model.UnitSystem;

public class UserSignUp extends JFrame implements ISignUpView {

    /* ---------- GUI-designer fields ---------- */
    private JPanel signUpPane;
    private JTextField fullNameField;
    private JButton cancelButton;
    private JTextField ageField;
    private JComboBox<Sex> sexCombo;
    private JFormattedTextField dobField;
    private JTextField heightField;
    private JTextField weightField;
    private JComboBox<UnitSystem> unitSystemCombo;
    private JButton createProfileButton;

    /* ---------- presenter hook ---------- */
    private Runnable onSubmit;   // set via setOnSubmit()

    /* ---------- ctor ---------- */
    public UserSignUp() {
        setTitle("User Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setContentPane(signUpPane);
        hookButtons();
        pack();
        setLocationRelativeTo(null);
        configureDobMask();
    }

    /* ---------- ISignUpView ---------- */
    @Override
    public void setOnSubmit(Runnable callback) {
        this.onSubmit = callback;
    }

    @Override
    public RawInput getFormInput() {
        // Special handling for dobField to get clean value
        String dobText = "";
        if (dobField.getValue() != null) {
            dobText = dobField.getValue().toString();
        } else {
            dobText = dobField.getText().replace("_", "").trim();
        }

        return new RawInput(
                fullNameField.getText().trim(),           // fullName
                ageField.getText().trim(),                // age
                dobText,                                  // dob (3rd position - correct!)
                heightField.getText().trim(),             // height
                weightField.getText().trim(),             // weight
                ((Sex) sexCombo.getSelectedItem()).name(), // sex (6th position - was incorrectly 3rd)
                ((UnitSystem) unitSystemCombo.getSelectedItem()).name()); // unitSystem
    }

    @Override
    public void showError(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Validation error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void close() { dispose(); }

    /* ---------- helper wiring ---------- */
    private void hookButtons() {
        createProfileButton.addActionListener(e -> {
            if (onSubmit != null) onSubmit.run();
        });
        cancelButton.addActionListener(e -> dispose());
    }

    private void configureDobMask() {
        try {
            MaskFormatter formatter = new MaskFormatter("####-##-##");
            formatter.setPlaceholderCharacter('_');
            formatter.setValueContainsLiteralCharacters(false); // This ensures getText() returns clean value
            dobField.setFormatterFactory(new DefaultFormatterFactory(formatter));
            dobField.setToolTipText("Enter date in format: YYYY-MM-DD (e.g., 1990-12-25)");
        } catch (ParseException e) {
            // If mask fails, just set tooltip
            dobField.setToolTipText("Please enter date in format: YYYY-MM-DD");
        }
    }

    /* ---------- GUI component initialization ---------- */
    private void initializeComponents() {
        // Create main panel
        signUpPane = new JPanel();
        signUpPane.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Create and add components
        fullNameField = new JTextField(20);
        ageField = new JTextField(20);
        heightField = new JTextField(20);
        weightField = new JTextField(20);

        // Create combo boxes with enum values
        sexCombo = new JComboBox<>();
        for (Sex sex : Sex.values()) {
            sexCombo.addItem(sex);
        }

        unitSystemCombo = new JComboBox<>();
        for (UnitSystem u : UnitSystem.values()) {
            unitSystemCombo.addItem(u);
        }

        // Create date field with tooltip
        dobField = new JFormattedTextField();
        dobField.setColumns(20);
        dobField.setToolTipText("Enter date in format: YYYY-MM-DD (e.g., 1990-12-25)");

        // Create buttons
        createProfileButton = new JButton("Create Profile");
        cancelButton = new JButton("Cancel");

        // Add labels and fields to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(sexCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(heightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Weight:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        signUpPane.add(new JLabel("Unit System:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(unitSystemCombo, gbc);

        // Add buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        signUpPane.add(createProfileButton, gbc);
        gbc.gridx = 1;
        signUpPane.add(cancelButton, gbc);
    }
}
