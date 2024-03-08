package journey.miles.api.infra.exception;

public class InvalidImageSizeException extends RuntimeException {
    public InvalidImageSizeException(String message) {
        super(message);
    }
}
