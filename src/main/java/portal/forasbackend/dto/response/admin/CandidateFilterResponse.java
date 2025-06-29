package portal.forasbackend.dto.response.admin;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.enums.Gender;

import java.util.List;

@Data
@Builder
public class CandidateFilterResponse {
    private Long id;
    private String name;
    private String phone;
    private String cityName;
    private Gender gender;
    private boolean knowsHebrew;
    private boolean needsHelp;
    private List<String> skills;
    private List<String> driverLicenses;
    private List<String> languages;
    private String avatarUrl;
}