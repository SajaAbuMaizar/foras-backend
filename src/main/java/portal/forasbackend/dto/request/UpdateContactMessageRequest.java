package portal.forasbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import portal.forasbackend.entity.ContactMessage.ContactMessageStatus;

@Data
public class UpdateContactMessageRequest {

    @NotNull(message = "Status is required")
    private ContactMessageStatus status;

    private String adminNotes;
}