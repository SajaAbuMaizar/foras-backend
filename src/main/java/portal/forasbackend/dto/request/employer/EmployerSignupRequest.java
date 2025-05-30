package portal.forasbackend.dto.request.employer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployerSignupRequest {
    @NotBlank
    private String name;
    @NotBlank private String companyName;
    @Email
    private String email;
    @NotBlank private String phone;
    @NotBlank private String password;
}
