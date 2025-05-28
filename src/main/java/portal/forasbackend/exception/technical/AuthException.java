package portal.forasbackend.exception.technical;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}