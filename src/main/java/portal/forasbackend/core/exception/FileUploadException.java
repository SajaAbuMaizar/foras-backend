package portal.forasbackend.core.exception;

public class FileUploadException extends RuntimeException {
    private final String errorCode;

    public FileUploadException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FileUploadException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}