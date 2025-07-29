package portal.forasbackend.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.application.dto.response.employer.EmployerSummaryDTO;
import portal.forasbackend.domain.model.Employer;

@Mapper(componentModel = "spring")
public interface EmployerMapper {
    @Mapping(target = "companyLogoUrl", source = "companyLogoUrl") // ✅ Explicit mapping
    EmployerSummaryDTO toSummaryDto(Employer employer);
}
