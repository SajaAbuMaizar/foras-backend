package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import java.time.format.DateTimeFormatter;
import portal.forasbackend.dto.LocalizedNameDto;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobTranslation;
import portal.forasbackend.enums.JobStatus;

import java.util.Optional;

@Data
@Builder
public class AdminJobDetailsResponse {
    private Long id;
    private String imageUrl;
    private String salary;
    private String jobType;
    private boolean transportationAvailable;
    private boolean hebrewRequired;
    private String createdAt;
    private LocalizedNameDto cityName;
    private LocalizedNameDto industryName;

    private String titleOriginal;
    private String titleTranslated;
    private String descriptionOriginal;
    private String descriptionTranslated;
    private String qualificationsOriginal;
    private String qualificationsTranslated;

    private JobStatus status;


    public static AdminJobDetailsResponse from(Job job) {
        JobTranslation original = job.getTranslations().stream()
                .filter(JobTranslation::isOriginal)
                .findFirst().orElse(null);

        JobTranslation translated = job.getTranslations().stream()
                .filter(t -> !t.isOriginal())
                .findFirst().orElse(null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedCreatedAt = job.getCreatedAt().format(formatter);

        return AdminJobDetailsResponse.builder()
                .id(job.getId())
                .imageUrl(job.getImageUrl())
                .salary(job.getSalary())
                .jobType(job.getJobType())
                .transportationAvailable(job.isTransportationAvailable())
                .hebrewRequired(job.isHebrewRequired())
                .createdAt(formattedCreatedAt)
                .industryName(LocalizedNameDto.from(job.getIndustry()))
                .cityName(LocalizedNameDto.from(job.getCity()))
                .titleOriginal(original != null ? original.getTitle() : "")
                .titleTranslated(translated != null ? translated.getTitle() : null)
                .descriptionOriginal(original != null ? original.getDescription() : "")
                .descriptionTranslated(translated != null ? translated.getDescription() : null)
                .qualificationsOriginal(original != null ? original.getRequiredQualifications() : "")
                .qualificationsTranslated(translated != null ? translated.getRequiredQualifications() : null)
                .status(job.getStatus())
                .build();
    }
}