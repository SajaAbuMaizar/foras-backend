package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobTranslation;

@Mapper(componentModel = "spring", uses = {CityMapper.class, IndustryMapper.class, EmployerMapper.class})
public interface JobMapper {

    @Mapping(target = "id", source = "job.id")
    @Mapping(target = "employer", source = "job.employer")
    @Mapping(target = "cityName", source = "job.city.nameAr")
    @Mapping(target = "industryName", source = "job.industry.nameAr")
    @Mapping(target = "publishDate", source = "job.publishDate")
    @Mapping(target = "jobTitle", source = "translation.title")
    @Mapping(target = "jobDescription", source = "translation.description")
    @Mapping(target = "requiredQualifications", source = "translation.requiredQualifications")
    MainPageJobListResponse toDto(Job job, JobTranslation translation);
}
