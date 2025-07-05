package portal.forasbackend.dto.request;

import lombok.Data;
import portal.forasbackend.entity.ApplicationStatus;

@Data
public class UpdateStatusRequest {
    private ApplicationStatus status;
}