package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.entity.Job;

@Mapper(componentModel = "spring", uses = {CityMapper.class, IndustryMapper.class, EmployerMapper.class})
public interface JobMapper {

    @Mapping(target = "employer", source = "employer")
    @Mapping(target = "cityName", source = "city.nameAr")  // ✅ Correct mapping
    @Mapping(target = "industryName", source = "industry.nameAr") // ✅ Ensure correct industry mapping
    MainPageJobListResponse toDto(Job job);
}
