package portal.forasbackend.application.dto.request.candidate;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CandidateSignupRequestDTO {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Only English letters are allowed")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    @Size(min = 6)
    private String confirmPassword;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
