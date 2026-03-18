package exceptions;

public class BadResponseException extends RuntimeException {

    int responseCode;

    public BadResponseException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }
}
