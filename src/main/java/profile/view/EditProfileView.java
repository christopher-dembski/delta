package profile.view;

import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import profile.model.Profile;

/**
 * Panel-based implementation of the edit profile view 
 */
public class EditProfileView extends BaseFormView implements IEditProfileView {

    //presenter hooks
    private Runnable onSubmit;
    private Runnable onCancel;
    
    // Store the profile being edited
    private Profile currentProfile;

    //constructor
    public EditProfileView() {
        initializeCommonComponents("Update Profile", "Cancel", "Edit Profile");
        hookButtons();
    }

    @Override
    public void setOnSubmit(Runnable callback) {
        this.onSubmit = callback;
    }

    @Override
    public void setOnCancel(Runnable callback) {
        this.onCancel = callback;
    }

    @Override
    public EditInput getFormInput() {
        return new EditInput(
                currentProfile != null ? currentProfile.getId() : null, // id
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
        currentProfile = null;
    }

    @Override
    public void loadProfileData(Profile profile) {
        this.currentProfile = profile;
        if (profile != null) {
            fullNameField.setText(profile.getName());
            dobField.setText(profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            heightField.setText(String.valueOf(profile.getHeight()));
            weightField.setText(String.valueOf(profile.getWeight()));
            sexCombo.setSelectedItem(profile.getSex());
            unitSystemCombo.setSelectedItem(profile.getUnitSystem());
        }
    }

    //wiring helper
    private void hookButtons() {
        submitButton.addActionListener(e -> {
            if (onSubmit != null)
                onSubmit.run();
        });
        cancelButton.addActionListener(e -> {
            if (onCancel != null)
                onCancel.run();
        });
    }
}
