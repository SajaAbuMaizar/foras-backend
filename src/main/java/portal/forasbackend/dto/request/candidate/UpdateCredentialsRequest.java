package portal.forasbackend.dto.request.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCredentialsRequest {

    @NotBlank(message = "الاسم مطلوب")
    private String name;

    private String currentPassword;

    @Size(min = 6, message = "كلمة المرور يجب أن تكون 6 أحرف على الأقل")
    private String newPassword;
}