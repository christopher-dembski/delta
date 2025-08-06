package profile.view;

import javax.swing.JOptionPane;

/**
 * Panel-based implementation of the user sign up view 
 */
public class SignUpView extends BaseFormView implements ISignUpView {

    //presenter hook
    private Runnable onSubmit;

    //constructor
    public SignUpView() {
        initializeCommonComponents("Create Profile", "Clear Form", "Create New Profile");
        hookButtons();
    }

    @Override
    public void setOnSubmit(Runnable callback) {
        this.onSubmit = callback;
    }

    @Override
    public RawInput getFormInput() {
        return new RawInput(
                fullNameField.getText().trim(), // fullName
                dobField.getText().trim(), // dob
                heightField.getText().trim(), // height
                weightField.getText().trim(), // weight
                ((profile.model.Sex) sexCombo.getSelectedItem()).name(), // sex
                ((profile.model.UnitSystem) unitSystemCombo.getSelectedItem()).name()); // unitSystem
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
    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void close() {
        clearCommonForm();
    }

    //wiring helper
    private void hookButtons() {
        submitButton.addActionListener(e -> {
            if (onSubmit != null)
                onSubmit.run();
        });
        cancelButton.addActionListener(e -> clearCommonForm());
    }
}
