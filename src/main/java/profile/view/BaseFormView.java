package profile.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import profile.model.Sex;
import profile.model.UnitSystem;

/**
 * Abstract base class for form views to eliminate code duplication.
 * Contains common form field initialization and layout code.
 */
public abstract class BaseFormView extends JPanel {
    
    // Common form fields
    protected JTextField fullNameField;
    protected JTextField dobField;
    protected JTextField heightField;
    protected JTextField weightField;
    protected JComboBox<Sex> sexCombo;
    protected JComboBox<UnitSystem> unitSystemCombo;
    
    // Common buttons
    protected JButton submitButton;
    protected JButton cancelButton;
    
    /**
     * Initialize common form components and layout.
     * @param submitButtonText Text for the submit button
     * @param cancelButtonText Text for the cancel button
     * @param title Title for the form
     */
    protected void initializeCommonComponents(String submitButtonText, String cancelButtonText, String title) {
        // Set layout for the panel
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Create common components
        createCommonFormFields();
        
        // Create buttons
        submitButton = new JButton(submitButtonText);
        cancelButton = new JButton(cancelButtonText);

        // Add title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        add(new JLabel("<html><h2>" + title + "</h2></html>"), gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // Add components to panel using common layout
        addFormFieldsToLayout(gbc);
        
        // Add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    /**
     * Create common form fields with standard configuration.
     */
    private void createCommonFormFields() {
        fullNameField = new JTextField(20);
        fullNameField.setToolTipText("Enter your full name");

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
    }
    
    /**
     * Add form fields to the layout using common positioning.
     * @param gbc GridBagConstraints for layout
     */
    private void addFormFieldsToLayout(java.awt.GridBagConstraints gbc) {
        // FULL-NAME
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(fullNameField, gbc);

        //SEX
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Sex:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(sexCombo, gbc);

        //DOB
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(dobField, gbc);

        //UNIT SYSTEM
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Unit System:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(unitSystemCombo, gbc);

        //HEIGHT
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Height:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(heightField, gbc);

        //WEIGHT
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        add(new JLabel("Weight:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(weightField, gbc);
    }
    
    /**
     * Clear all form fields to their default state.
     */
    protected void clearCommonForm() {
        fullNameField.setText("");
        dobField.setText("");
        heightField.setText("");
        weightField.setText("");
        sexCombo.setSelectedIndex(0);
        unitSystemCombo.setSelectedIndex(0);
    }
} 