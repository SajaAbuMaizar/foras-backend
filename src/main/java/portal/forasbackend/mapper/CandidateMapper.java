package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.entity.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(target = "area", source = "city.nameAr")
    CandidateProfileDto toDto(Candidate candidate);
}