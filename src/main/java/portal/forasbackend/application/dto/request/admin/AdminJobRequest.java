package portal.forasbackend.application.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminJobRequest {
    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @NotBlank(message = "Language is required")
    private String language;

    @NotNull(message = "City ID is required")
    private Long cityId;

    @NotNull(message = "Job type is required")
    private Long jobTypeId;

    @NotNull(message = "Industry ID is required")
    private Long industryId;

    @NotBlank(message = "Salary is required")
    private String salary;

    @NotBlank(message = "Required qualifications are required")
    private String requiredQualifications;

    private boolean transportation;
    private boolean hebrew;

    // For fake employer creation
    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyDescription;
    private String companyPhone;
    private String companyEmail;
    private String companyLogoUrl;

    // Auto-approve option for seed data
    private boolean autoApprove = true;
}