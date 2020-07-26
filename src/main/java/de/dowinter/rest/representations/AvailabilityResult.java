package de.dowinter.rest.representations;

public class AvailabilityResult {
    boolean isAvailable;

    public AvailabilityResult(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
