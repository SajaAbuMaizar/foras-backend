package portal.forasbackend.application.dto.response.candidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDto {
    private String id;
    private String name;
    private String phone;
}
