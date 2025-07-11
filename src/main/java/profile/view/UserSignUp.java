package profile.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import profile.model.Sex;
import profile.model.UnitSystem;

public class UserSignUp extends JFrame implements ISignUpView {

    private JPanel signUpPane;
    private JTextField fullNameField;
    private JTextField ageField;
    private JTextField dobField;
    private JTextField heightField;
    private JTextField weightField;

    private JComboBox<Sex> sexCombo;
    private JComboBox<UnitSystem> unitSystemCombo;

    private JButton createProfileButton;
    private JButton cancelButton;

    //presenter hook
    private Runnable onSubmit;

    //constructor
    public UserSignUp() {
        setTitle("User Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setContentPane(signUpPane);
        hookButtons();
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void setOnSubmit(Runnable callback) {
        this.onSubmit = callback;
    }

    @Override
    public RawInput getFormInput() {
        return new RawInput(
                fullNameField.getText().trim(), // fullName
                ageField.getText().trim(), // age
                dobField.getText().trim(), // dob
                heightField.getText().trim(), // height
                weightField.getText().trim(), // weight
                ((Sex) sexCombo.getSelectedItem()).name(), // sex
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
    public void close() {
        dispose();
    }

    //wiring helper
    private void hookButtons() {
        createProfileButton.addActionListener(e -> {
            if (onSubmit != null)
                onSubmit.run();
        });
        cancelButton.addActionListener(e -> dispose());
    }

    // GUI initialization
    private void initializeComponents() {
        // Create main panel
        signUpPane = new JPanel();
        signUpPane.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Create components
        fullNameField = new JTextField(20);
        fullNameField.setToolTipText("Enter your full name");

        ageField = new JTextField(20);
        ageField.setToolTipText("Enter your age in years");

        heightField = new JTextField(20);
        heightField.setToolTipText("Enter your height");

        weightField = new JTextField(20);
        weightField.setToolTipText("Enter your weight");

        // Create combo boxes
        sexCombo = new JComboBox<>();
        for (Sex sex : Sex.values()) {
            sexCombo.addItem(sex);
        }

        unitSystemCombo = new JComboBox<>();
        for (UnitSystem u : UnitSystem.values()) {
            unitSystemCombo.addItem(u);
        }

        // Create date field
        dobField = new JTextField(20);
        dobField.setToolTipText("Enter date in format: YYYY-MM-DD (e.g., 1990-12-25)");

        // Create buttons
        createProfileButton = new JButton("Create Profile");
        cancelButton = new JButton("Cancel");

        // Add components to panel
        // FULL-NAME
        gbc.gridx = 0; //column position
        gbc.gridy = 0; //row position
        gbc.anchor = java.awt.GridBagConstraints.WEST; //alignment inside the cell
        signUpPane.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL; //stretch horizontally
        signUpPane.add(fullNameField, gbc);

        //AGE
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Age:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(ageField, gbc);

        //SEX
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Sex:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(sexCombo, gbc);

        //DOB
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(dobField, gbc);

        //UNIT SYSTEM
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Unit System:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(unitSystemCombo, gbc);

        //HEIGHT
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Height:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(heightField, gbc);

        //WEIGHT
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        signUpPane.add(new JLabel("Weight:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        signUpPane.add(weightField, gbc);

        // BUTTONS
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createProfileButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        signUpPane.add(buttonPanel, gbc);
    }
}
