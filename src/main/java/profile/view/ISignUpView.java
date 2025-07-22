
package profile.view;

public interface ISignUpView {

    /** Register a one-shot handler fired when the user hits “Create profile”. */
    void setOnSubmit(Runnable callback);

    /** @return freshly-typed but *unvalidated* user data. */
    RawInput getFormInput();

    /** Inform the user something went wrong. */
    void showError(String msg);

    /** Inform the user of successful profile creation. */
    void showSuccess(String msg);

    /** Close & dispose the window after success. */
    void close();

    /** Simple holder for raw text from the form. */
    record RawInput(
            String fullName,
            String dob,
            String height,
            String weight,
            String sex,
            String unitSystem) {}
}
