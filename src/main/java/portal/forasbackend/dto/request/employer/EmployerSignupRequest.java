package portal.forasbackend.dto.request.employer;

import jakarta.validation.constraints.*;
import lombok.Data;
import portal.forasbackend.validation.ValidPhone;

@Data
public class EmployerSignupRequest {
    @NotBlank(message = "الاسم مطلوب")
    @Size(min = 2, max = 100, message = "الاسم يجب أن يكون بين 2 و 100 حرف")
    private String name;

    @NotBlank(message = "اسم الشركة مطلوب")
    @Size(min = 2, max = 100, message = "اسم الشركة يجب أن يكون بين 2 و 100 حرف")
    private String companyName;

    @NotBlank(message = "البريد الإلكتروني مطلوب")
    @Email(message = "البريد الإلكتروني غير صالح")
    private String email;

    @NotBlank(message = "رقم الهاتف مطلوب")
    @ValidPhone
    private String phone;

    @NotBlank(message = "كلمة المرور مطلوبة")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
//            message = "كلمة المرور يجب أن تحتوي على 8 أحرف على الأقل، حرف كبير، حرف صغير، رقم ورمز خاص")
    private String password;
}