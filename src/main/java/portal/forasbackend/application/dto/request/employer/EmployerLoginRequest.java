package portal.forasbackend.application.dto.request.employer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployerLoginRequest {
    @NotBlank
    private String phone;

    @NotBlank
    private String password;

}