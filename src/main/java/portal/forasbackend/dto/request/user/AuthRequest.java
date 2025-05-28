package portal.forasbackend.dto.request.user;
import lombok.Data;

@Data
public class AuthRequest {
    private String phone;
    private String password;
}
