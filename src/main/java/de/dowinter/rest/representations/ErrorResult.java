package de.dowinter.rest.representations;

public class ErrorResult {
    String message;

    public ErrorResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
