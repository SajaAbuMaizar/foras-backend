package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.employer.EmployerSummaryDTO;
import portal.forasbackend.domain.employer.model.Employer;

@Mapper(componentModel = "spring")
public interface EmployerMapper {
    @Mapping(target = "companyLogoUrl", source = "companyLogoUrl") // ✅ Explicit mapping
    EmployerSummaryDTO toSummaryDto(Employer employer);
}
