package profile.view;

/**
 * Simple test implementation of ISignUpView for unit testing
 */
public class TestSignUpView implements ISignUpView {
    private Runnable submitCallback;
    private RawInput formInput;
    private String lastError;
    private boolean closed = false;

    public TestSignUpView() {
        // Default test data
        this.formInput = new RawInput(
            "John Doe",
            "25",
            "1998-01-15",
            "175.0",
            "70.0",
            "MALE",
            "METRIC"
        );
    }

    @Override
    public void setOnSubmit(Runnable callback) {
        this.submitCallback = callback;
    }

    @Override
    public RawInput getFormInput() {
        return formInput;
    }

    @Override
    public void showError(String msg) {
        this.lastError = msg;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    // Test helper methods
    public void setFormInput(RawInput input) {
        this.formInput = input;
    }

    public void triggerSubmit() {
        if (submitCallback != null) {
            submitCallback.run();
        }
    }

    public String getLastError() {
        return lastError;
    }

    public boolean isClosed() {
        return closed;
    }

    public void reset() {
        this.lastError = null;
        this.closed = false;
    }
}
