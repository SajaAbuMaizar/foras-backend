package portal.forasbackend.exception.business;

public class InvalidGenderException extends RuntimeException {
    public InvalidGenderException(String invalidValue) {
        super("Invalid gender value: " + invalidValue + ". Valid values are: MALE, FEMALE, OTHER");
    }
}