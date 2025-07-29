package portal.forasbackend.application.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.application.dto.LocalizedNameDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobListResponse {
    private String id;
    private String jobTitle;
    private LocalizedNameDto cityName;
    private String jobDescription;
    private String salary;
    private String status;
}