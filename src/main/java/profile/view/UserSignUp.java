package profile.view;

import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

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
    private JLabel heightLabel;
    private JLabel weightLabel;

    /* ---------- presenter hook ---------- */
    private Runnable onSubmit;   // set via setOnSubmit()

    /* ---------- ctor ---------- */
    public UserSignUp() {
        setTitle("User Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setContentPane(signUpPane);
        hookButtons();
        setupUnitSystemListener();
        updateLabelsForUnitSystem();
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
        String dobText;
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

    private void setupUnitSystemListener() {
        unitSystemCombo.addActionListener(e -> updateLabelsForUnitSystem());
    }

    private void updateLabelsForUnitSystem() {
        UnitSystem selectedUnit = (UnitSystem) unitSystemCombo.getSelectedItem();
        if (selectedUnit == UnitSystem.METRIC) {
            heightLabel.setText("Height (cm):");
            weightLabel.setText("Weight (kg):");
            heightField.setToolTipText("Enter your height in centimeters (e.g., 175)");
            weightField.setToolTipText("Enter your weight in kilograms (e.g., 70.5)");
        } else { // IMPERIAL
            heightLabel.setText("Height (ft/in):");
            weightLabel.setText("Weight (lb):");
            heightField.setToolTipText("Enter your height in feet and inches (e.g., 5'9\" or 5.75)");
            weightField.setToolTipText("Enter your weight in pounds (e.g., 155)");
        }
    }

    /* ---------- GUI component initialization ---------- */
    private void initializeComponents() {
        // Create main panel
        signUpPane = new JPanel();
        signUpPane.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Create and add components with tooltips
        fullNameField = new JTextField(20);
        fullNameField.setToolTipText("Enter your full name (e.g., John Doe)");
        
        ageField = new JTextField(20);
        ageField.setToolTipText("Enter your age in years (e.g., 25)");
        
        heightField = new JTextField(20);
        heightField.setToolTipText("Enter your height (units will be shown based on selected unit system)");
        
        weightField = new JTextField(20);
        weightField.setToolTipText("Enter your weight (units will be shown based on selected unit system)");

        // Create combo boxes with enum values
        sexCombo = new JComboBox<>();
        for (Sex sex : Sex.values()) {
            sexCombo.addItem(sex);
        }
        sexCombo.setToolTipText("Select your biological sex");

        unitSystemCombo = new JComboBox<>();
        for (UnitSystem u : UnitSystem.values()) {
            unitSystemCombo.addItem(u);
        }
        unitSystemCombo.setToolTipText("Choose the unit system for height and weight measurements");

        // Create date field with tooltip
        dobField = new JFormattedTextField();
        dobField.setColumns(20);
        dobField.setToolTipText("Enter date in format: YYYY-MM-DD (e.g., 1990-12-25)");

        // Create buttons with colors
        createProfileButton = new JButton("Create Profile");
        createProfileButton.setBackground(new java.awt.Color(46, 125, 50)); // Green
        createProfileButton.setForeground(java.awt.Color.WHITE);
        createProfileButton.setFocusPainted(false);
        createProfileButton.setOpaque(true);
        createProfileButton.setBorderPainted(false);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new java.awt.Color(198, 40, 40)); // Red
        cancelButton.setForeground(java.awt.Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);

        // Create labels that will be updated dynamically
        heightLabel = new JLabel("Height (ft/in):"); // Start with longer text to reserve space
        weightLabel = new JLabel("Weight (lb):"); // Start with longer text to reserve space

        // Add labels and fields to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(sexCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(new JLabel("Unit System:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(unitSystemCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(heightLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(heightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signUpPane.add(weightLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(weightField, gbc);

        // Add buttons - create a button panel to center them
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createProfileButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        signUpPane.add(buttonPanel, gbc);
    }
}
