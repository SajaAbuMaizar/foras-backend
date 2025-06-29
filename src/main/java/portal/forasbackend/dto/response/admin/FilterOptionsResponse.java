package portal.forasbackend.dto.response.admin;

import lombok.Builder;
import lombok.Data;
import portal.forasbackend.enums.Gender;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class FilterOptionsResponse {
    private List<Gender> availableGenders;
    private List<String> availableSkills;
    private List<String> availableDriverLicenses;
    private List<String> availableLanguages;
    private Map<String, String> availableCities; // code -> name mapping
}