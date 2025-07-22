package shared.service_output;

public record ServiceError(String message) {
    @Override public String toString() {
        return "ServiceError(%s)".formatted(message);
    }
}
