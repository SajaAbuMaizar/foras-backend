package portal.forasbackend.dto.response.employer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerLogoUrlDTO {
    private Long id;
    private String companyLogoUrl;
}
