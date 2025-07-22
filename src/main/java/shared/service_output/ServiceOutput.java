package shared.service_output;

import java.util.List;

public abstract class ServiceOutput {
    private final List<ServiceError> errors;

    public ServiceOutput(List<ServiceError> errors) {
        this.errors = errors;
    }

    public List<ServiceError> errors() {
        return errors;
    }

    public boolean ok() {
        return errors.isEmpty();
    }
}
