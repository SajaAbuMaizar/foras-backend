package portal.forasbackend.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import portal.forasbackend.application.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.domain.model.Job;
import portal.forasbackend.domain.model.JobTranslation;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mappings({
            @Mapping(target = "id", source = "job.id"),
            @Mapping(target = "imageUrl", source = "job.imageUrl"),
            @Mapping(target = "salary", source = "job.salary"),
            @Mapping(target = "jobTitle", source = "translation.title"),
            @Mapping(target = "jobDescription", source = "translation.description"),
            @Mapping(target = "requiredQualifications", source = "translation.requiredQualifications"),
            @Mapping(target = "hebrewRequired", source = "job.hebrewRequired"),
            @Mapping(target = "transportationAvailable", source = "job.transportationAvailable"),
            @Mapping(target = "jobTypeName", source = "job.jobType.nameAr"),
            @Mapping(target = "cityName", source = "job.city.nameAr"),
            @Mapping(target = "industryName", source = "job.industry.nameAr"),
            @Mapping(target = "latitude", source = "job.latitude"),
            @Mapping(target = "longitude", source = "job.longitude"),
            @Mapping(target = "publishDate", expression = "java(job.getPublishDate() != null ? job.getPublishDate().toString() : null)"),
            @Mapping(target = "employer", expression = "java(mapEmployer(job))")
    })
    MainPageJobListResponse toDto(Job job, JobTranslation translation);

    // Default method for nested mapping
    default MainPageJobListResponse.EmployerDto mapEmployer(Job job) {
        if (job.getEmployer() == null) return null;

        return MainPageJobListResponse.EmployerDto.builder()
                .id(job.getEmployer().getId())
                .companyName(job.getEmployer().getCompanyName())
                .companyLogoUrl(job.getEmployer().getCompanyLogoUrl())
                .build();
    }
}
