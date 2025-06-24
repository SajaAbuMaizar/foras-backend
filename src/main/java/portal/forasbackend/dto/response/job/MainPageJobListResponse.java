package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.dto.response.employer.EmployerSummaryDTO;

import java.time.LocalDate;

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

    private EmployerSummaryDTO employer;

    private LocalDate publishDate;
}