package portal.forasbackend.application.dto.response.employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerSummaryDTO {
    private Long id;
    private String companyName;
    private String companyLogoUrl;
}