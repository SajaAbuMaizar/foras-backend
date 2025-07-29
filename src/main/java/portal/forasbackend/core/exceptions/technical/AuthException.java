package portal.forasbackend.core.exceptions.technical;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}