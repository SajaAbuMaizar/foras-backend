package portal.forasbackend.dto.request.job;
import lombok.Data;

@Data
public class JobSearchRequest {
    private String city; // City code
    private String industry; // Industry code
    private Boolean hebrewRequired;
    private Boolean transportationAvailable;
    private Integer page = 0;
    private Integer size = 10;
}