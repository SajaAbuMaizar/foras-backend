package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.entity.City;

@Data
@Builder
public class EmployerDashboardJobListResponse {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String salary;
    private City cityName;
}
