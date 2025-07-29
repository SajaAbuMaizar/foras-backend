package portal.forasbackend.application.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.domain.model.City;
import portal.forasbackend.domain.enums.JobStatus;

@Data
@Builder
public class EmployerDashboardJobListResponse {
    private Long id;
    private String jobTitle;
    private City cityName;
    private String jobDescription;
    private String salary;
    private JobStatus status;

}
