package portal.forasbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "saved_jobs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "candidate_id"})
)
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    @PrePersist
    public void prePersist() {
        this.savedAt = LocalDateTime.now();
    }
}