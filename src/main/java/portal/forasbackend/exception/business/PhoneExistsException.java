package portal.forasbackend.exception.business;

public class PhoneExistsException extends RuntimeException {
    public PhoneExistsException(String phone) {
        super("Phone " + phone + " already exists");
    }
}