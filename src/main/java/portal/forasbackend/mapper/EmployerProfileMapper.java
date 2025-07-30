package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import portal.forasbackend.dto.request.employer.UpdateEmployerProfileRequest;
import portal.forasbackend.dto.response.employer.EmployerProfileResponse;
import portal.forasbackend.domain.model.Employer;

@Mapper(componentModel = "spring")
public interface EmployerProfileMapper {

    EmployerProfileResponse toProfileResponse(Employer employer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "companyLogoUrl", ignore = true)
    void updateEmployerFromRequest(UpdateEmployerProfileRequest request, @MappingTarget Employer employer);
}