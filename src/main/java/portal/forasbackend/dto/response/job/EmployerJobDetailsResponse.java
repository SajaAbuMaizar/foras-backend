package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.dto.LocalizedNameDto;
import portal.forasbackend.dto.response.candidate.CandidateDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobTranslation;
import portal.forasbackend.enums.JobStatus;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class EmployerJobDetailsResponse {
    private Long id;
    private String imageUrl;
    private String salary;
    private String jobTitle;
    private String jobDescription;
    private String jobType;
    private String requiredQualifications;
    private boolean hebrewRequired;
    private boolean transportationAvailable;
    private String createdAt;
    private LocalizedNameDto industryName;
    private LocalizedNameDto cityName;
    private List<CandidateDto> candidates;
    private JobStatus status;


    public static EmployerJobDetailsResponse from(Job job) {
        // Find the original translation (assuming only one original per job)
        JobTranslation originalTranslation = job.getTranslations().stream()
                .filter(JobTranslation::isOriginal)
                .findFirst()
                .orElse(null);

        String title = originalTranslation != null ? originalTranslation.getTitle() : "";
        String description = originalTranslation != null ? originalTranslation.getDescription() : "";
        String requiredQualifications = originalTranslation != null ? originalTranslation.getRequiredQualifications() : "";


        return EmployerJobDetailsResponse.builder()
                .id(job.getId())
                .imageUrl(job.getImageUrl())
                .salary(job.getSalary())
                .jobTitle(title)
                .jobDescription(description)
                .jobType(job.getJobType())
                .requiredQualifications(requiredQualifications)
                .hebrewRequired(job.isHebrewRequired())
                .transportationAvailable(job.isTransportationAvailable())
                .createdAt(job.getCreatedAt().toString())
                .industryName(LocalizedNameDto.from(job.getIndustry()))
                .cityName(LocalizedNameDto.from(job.getCity()))
                .status(job.getStatus())
                .candidates(job.getApplications().stream()
                        .map(app -> new CandidateDto(
                                String.valueOf(app.getCandidate().getId()),
                                app.getCandidate().getName(),
                                app.getCandidate().getPhone()
                        ))
                        .collect(Collectors.toList()))
                .build();
    }

}