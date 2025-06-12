package portal.forasbackend.dto.response.candidate;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CandidateProfileDto {
    private Long id;
    private String name;
    private String phone;
    private String area;
    private boolean knowsHebrew;
    private boolean needsHelp;
    private List<String> driverLicenses;
    private List<String> skills;
    private List<String> languages;
    private String avatarUrl;
}
