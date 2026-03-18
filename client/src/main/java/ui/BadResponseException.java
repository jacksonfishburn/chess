package ui;

public class BadResponseException extends RuntimeException {

    int responseCode;

    public BadResponseException(String message, int responseCode) {
        this.responseCode = responseCode;
        super(message);
    }
}
