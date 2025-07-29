package portal.forasbackend.core.exceptions.business;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String cityCode) {
        super("City not found with code: " + cityCode);
    }
}