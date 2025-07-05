package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ContactMessageStatus status = ContactMessageStatus.NEW;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "read")
    @Builder.Default
    private boolean read = false;

    @Column(name = "handled_by")
    private Long handledBy;

    @Column(name = "handled_at")
    private LocalDateTime handledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ContactMessageStatus {
        NEW,
        IN_PROGRESS,
        DONE,
        ARCHIVED
    }
}