package portal.forasbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.domain.model.ContactMessage.ContactMessageStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String subject;
    private String message;
    private ContactMessageStatus status;
    private String adminNotes;
    private boolean read;
    private Long handledBy;
    private LocalDateTime handledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}