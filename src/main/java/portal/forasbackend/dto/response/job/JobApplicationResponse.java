package portal.forasbackend.dto.response.job;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.entity.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobApplicationResponse {
    private Long id;
    private Long jobId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String candidateAvatar;
    private String candidateLocation;
    private List<String> skills;
    private String coverLetter;
    private String resumeUrl;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private String experience;
    private String education;
    private List<String> languages;
}