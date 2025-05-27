package portal.forasbackend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import portal.forasbackend.exception.business.CityNotFoundException;
import portal.forasbackend.exception.business.InvalidGenderException;
import portal.forasbackend.exception.business.PhoneExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PhoneExistsException.class,
            CityNotFoundException.class,
            InvalidGenderException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    // Standardized error response
    public record ErrorResponse(String message) {}
}

