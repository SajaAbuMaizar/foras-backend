package portal.forasbackend.dto.request.job;

import lombok.Data;

@Data
public class JobRequest {
    private String jobTitle;
    private String jobDescription;
    private Long cityId;
    private String jobType;
    private Long industryId;
    private String salary;
    private String requiredQualifications;
    private boolean transportation;
    private boolean hebrew;
    private Double latitude;
    private Double longitude;
}
