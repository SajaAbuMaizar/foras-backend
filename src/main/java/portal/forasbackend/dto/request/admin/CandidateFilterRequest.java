package portal.forasbackend.dto.request.admin;

import lombok.Data;
import portal.forasbackend.enums.Gender;

import java.util.List;

@Data
public class CandidateFilterRequest {
    private List<Gender> genders;
    private List<String> skills;
    private List<String> driverLicenses;
    private List<String> languages;
    private List<String> cities; // City codes
    private Boolean knowsHebrew;
    private Boolean needsHelp;
    private String searchQuery; // For name or phone search

    // Pagination
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "name"; // name, phone, city
    private String sortDirection = "asc"; // asc, desc
}