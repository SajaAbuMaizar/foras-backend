package portal.forasbackend.application.dto.request;

import lombok.Data;
import portal.forasbackend.domain.model.ApplicationStatus;

@Data
public class UpdateStatusRequest {
    private ApplicationStatus status;
}