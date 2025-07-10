package swaps.view;

public enum DropdownOptionIntensity {
    HIGH("A lot"),
    MEDIUM("A moderate amount"),
    LOW("A little");

    private final String label;

    DropdownOptionIntensity(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
