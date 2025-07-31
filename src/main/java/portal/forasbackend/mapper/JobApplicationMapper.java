package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.job.JobApplicationResponse;
import portal.forasbackend.domain.application.model.JobApplication;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    @Mapping(target = "candidateName", expression = "java(getCandidateName(application))")
    @Mapping(target = "candidatePhone", source = "candidate.phone")
    @Mapping(target = "candidateGender", source = "candidate.gender")
    @Mapping(target = "candidateAvatar", source = "candidate.avatarUrl")
    @Mapping(target = "candidateLocation", expression = "java(getCandidateLocation(application))")
    @Mapping(target = "skills", expression = "java(getCandidateSkills(application))")
    @Mapping(target = "languages", expression = "java(getCandidateLanguages(application))")
//    @Mapping(target = "driverLicenses", source = "candidate.driverLicenses")
//    @Mapping(target = "resumeUrl", source = "candidate.resumeUrl")
//    @Mapping(target = "education", source = "candidate.education")
//    @Mapping(target = "experience", source = "candidate.experience")
//    @Mapping(target = "coverLetter", source = "coverLetter")
    @Mapping(target = "appliedAt", source = "createdAt")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "candidateId", source = "candidate.id")
    JobApplicationResponse toResponse(JobApplication application);

    default String getCandidateName(JobApplication application) {
        return application.getCandidate().getName();
    }

    default String getCandidateLocation(JobApplication application) {
        if (application.getCandidate().getCity() != null) {
            return application.getCandidate().getCity().getNameAr();
        }
        return null;
    }

    default List<String> getCandidateSkills(JobApplication application) {
        return application.getCandidate().getSkills();
    }

    default List<String> getCandidateLanguages(JobApplication application) {
        return application.getCandidate().getLanguages();
    }

//    default List<String> getCandidateSkills(JobApplication application) {
//        return application.getCandidate().getSkills().stream()
//                .map(skill -> skill.name())
//                .collect(Collectors.toList());
//    }
//
//    default List<String> getCandidateLanguages(JobApplication application) {
//        return application.getCandidate().getLanguages().stream()
//                .map(language -> language.name())
//                .collect(Collectors.toList());
//    }
}