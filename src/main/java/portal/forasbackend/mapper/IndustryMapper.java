package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import portal.forasbackend.dto.response.shared.IndustryDTO;
import portal.forasbackend.entity.Industry;

@Mapper(componentModel = "spring")
public interface IndustryMapper {
    IndustryDTO toDto(Industry industry);
}