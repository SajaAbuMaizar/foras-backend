package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MainPageJobListResponse {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String salary;
    private String requiredQualifications;
    private String jobType;
    private String imageUrl;
    private boolean transportationAvailable;
    private boolean hebrewRequired;
    private String cityName;
    private String industryName;
    private Double latitude;
    private Double longitude;

    private Long employerId;
    private String employerCompanyName;
    private String employerCompanyLogoUrl;

    private String postedDate;
}
