package portal.forasbackend.dto.request.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    private String jobTitle;
    private String jobDescription;
    private String language;
    private Long cityId;
    private Long jobTypeId;
    private Long industryId;
    private String salary;
    private String requiredQualifications;
    private boolean transportation;
    private boolean hebrew;
    private Double latitude;
    private Double longitude;
}