package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.entity.City;
import portal.forasbackend.enums.JobStatus;

@Data
@Builder
public class AdminDashboardJobListResponse {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String salary;
    private City cityName;
    private JobStatus status;
}