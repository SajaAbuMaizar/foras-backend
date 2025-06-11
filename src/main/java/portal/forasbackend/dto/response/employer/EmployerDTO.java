package portal.forasbackend.dto.response.employer;

import lombok.Builder;

@Builder
public record EmployerDTO(
        Long id,
        String name,
        String companyLogoUrl,
        String type
) {}