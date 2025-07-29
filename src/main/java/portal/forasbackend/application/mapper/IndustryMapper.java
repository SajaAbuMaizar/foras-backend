package portal.forasbackend.application.mapper;

import org.mapstruct.Mapper;
import portal.forasbackend.application.dto.response.shared.IndustryDTO;
import portal.forasbackend.domain.model.Industry;

@Mapper(componentModel = "spring")
public interface IndustryMapper {
    IndustryDTO toDto(Industry industry);
}