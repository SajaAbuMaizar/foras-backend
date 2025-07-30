package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import portal.forasbackend.dto.response.shared.CityDTO;
import portal.forasbackend.domain.model.City;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityDTO toDto(City city);
}