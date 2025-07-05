package portal.forasbackend.dto.request.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import portal.forasbackend.enums.Gender;

import java.util.List;

@Data
public class UpdateCandidateProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Hebrew knowledge status is required")
    private Boolean knowsHebrew;

    @NotNull(message = "Help needed status is required")
    private Boolean needsHelp;

    private List<String> skills;
    private List<String> languages;
    private List<String> driverLicenses;
}