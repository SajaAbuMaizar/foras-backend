package portal.forasbackend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.dto.response.employer.EmployerSummaryDTO;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Industry;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchResponseDTO {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String salary;
    private String requiredQualifications;
    private String jobType;
    private String imageUrl;
    private boolean transportationAvailable;
    private boolean hebrewRequired;
    private City city;
    private Industry industry;
    private EmployerSummaryDTO employer;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
