package portal.forasbackend.application.dto.response.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.domain.enums.Gender;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDto {
    private String id;
    private String name;
    private String phone;
    private String area;
    private Gender gender;
    private Boolean knowsHebrew;
    private Boolean needsHelp;
    private List<String> skills;
    private List<String> languages;
    private List<String> driverLicenses;
    private String avatarUrl;
}