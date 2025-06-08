package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.dto.LocalizedNameDto;
import portal.forasbackend.dto.response.candidate.CandidateDto;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Job;

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

    public static EmployerJobDetailsResponse from(Job job) {
        return EmployerJobDetailsResponse.builder()
                .id(job.getId())
                .imageUrl(job.getImageUrl())
                .salary(job.getSalary())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .jobType(job.getJobType())
                .requiredQualifications(job.getRequiredQualifications())
                .hebrewRequired(job.isHebrewRequired())
                .transportationAvailable(job.isTransportationAvailable())
                .createdAt(job.getCreatedAt().toString())
                .industryName(LocalizedNameDto.from(job.getIndustry()))
                .cityName(LocalizedNameDto.from(job.getCity()))
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
