package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import portal.forasbackend.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salary;
    private String jobType;
    private String imageUrl;
    private boolean transportationAvailable;
    private boolean hebrewRequired;

    @ManyToOne
    private City city;

    @ManyToOne
    private Industry industry;

    @ManyToOne
    private Employer employer;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobTranslation> translations;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING;

    private LocalDateTime approvedAt;
    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = JobStatus.PENDING;
        }
    }

    public boolean isVisible() {
        return this.status == JobStatus.APPROVED;
    }

    public void approve() {
        this.status = JobStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        this.status = JobStatus.REJECTED;
        this.rejectionReason = reason;
        this.approvedAt = null;
    }
}
