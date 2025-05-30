package portal.forasbackend.dto.request.candidate;
import lombok.Data;

@Data
public class AuthRequest {
    private String phone;
    private String password;
}
