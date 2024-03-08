package journey.miles.api.infra.exception;

public class UnsupportedFileExtensionException extends RuntimeException {
    public UnsupportedFileExtensionException(String message) {
        super(message);
    }
}
