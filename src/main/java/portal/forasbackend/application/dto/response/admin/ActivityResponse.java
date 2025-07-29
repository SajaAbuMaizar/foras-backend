package portal.forasbackend.application.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private String id;
    private String type;
    private String description;
    private String createdAt;
    private String userId;
    private String userName;
}