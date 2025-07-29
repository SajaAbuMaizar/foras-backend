package portal.forasbackend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.domain.enums.JobStatus;

import java.time.LocalDate;
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

    private String imageUrl;
    private boolean transportationAvailable;
    private boolean hebrewRequired;

    @ManyToOne
    private City city;

    @ManyToOne
    private Industry industry;

    @ManyToOne
    private JobType jobType;

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

    private String rejectionReason;

    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private Admin approvedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDate publishDate;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.publishDate == null) {
            this.publishDate = LocalDate.now();
        }

        if (this.status == null) {
            this.status = JobStatus.PENDING;
        }
    }

    public boolean isVisible() {
        return this.status == JobStatus.APPROVED;
    }

    public void approve(Admin admin) {
        this.status = JobStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.rejectionReason = null;
        this.approvedBy = admin;
    }

    public void reject(String reason) {
        this.status = JobStatus.REJECTED;
        this.rejectionReason = reason;
        this.approvedAt = null;
        this.approvedBy = null;
    }

    public void updatePublishDate() {
        this.publishDate = LocalDate.now();
    }
}