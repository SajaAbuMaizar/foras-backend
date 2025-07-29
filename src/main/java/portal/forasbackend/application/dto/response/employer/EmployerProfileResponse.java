package portal.forasbackend.application.dto.response.employer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfileResponse {
    private Long id;
    private String name;
    private String companyName;
    private String email;
    private String phone;
    private String companyLogoUrl;
    private String preferredLanguage;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}