package portal.forasbackend.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryDTO {
    private Long id;
    private String nameAr;
    private String nameEn;
    private String code;
}