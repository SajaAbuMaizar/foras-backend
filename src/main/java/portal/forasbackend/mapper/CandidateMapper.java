package portal.forasbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import portal.forasbackend.dto.response.candidate.CandidateProfileDto;
import portal.forasbackend.domain.model.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(target = "area", source = "city.nameAr")
    @Mapping(target = "id", expression = "java(String.valueOf(candidate.getId()))")
    CandidateProfileDto toDto(Candidate candidate);
}