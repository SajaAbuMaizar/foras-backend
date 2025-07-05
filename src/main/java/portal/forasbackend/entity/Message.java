package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_type")
    private String senderType;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "recipient_type")
    private String recipientType;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "read")
    private boolean read = false;

    @Column(name = "sent")
    private boolean sent = false;

    @Column(name = "archived")
    private boolean archived = false;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "responded")
    private boolean responded = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}