package portal.forasbackend.application.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminAuthRequest {
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;
}