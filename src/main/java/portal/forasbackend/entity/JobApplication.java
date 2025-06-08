package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "candidate_id"}))
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Job job;

    @ManyToOne(optional = false)
    private Candidate candidate;

    private long appliedAt;

    @PrePersist
    public void prePersist() {
        this.appliedAt = System.currentTimeMillis();
    }}
