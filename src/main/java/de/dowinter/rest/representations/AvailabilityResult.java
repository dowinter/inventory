package de.dowinter.rest.representations;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class AvailabilityResult {
    boolean isAvailable;

    @JsonbCreator
    public AvailabilityResult(@JsonbProperty("isAvailable") boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
