package portal.forasbackend.core.exception.business;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneExistsException extends RuntimeException {
    public PhoneExistsException(String phone) {
        super("Phone " + phone + " already exists");
    }
}