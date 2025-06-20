package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.enums.JobStatus;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Job details (existing fields)
    private String jobTitle;
    @Column(length = 1000)
    private String jobDescription;
    private String salary;
    @Column(length = 1000)
    private String requiredQualifications;
    private String jobType;
    private String imageUrl;
    private boolean transportationAvailable;
    private boolean hebrewRequired;

    // Relations (existing fields)
    @ManyToOne
    private City city;
    @ManyToOne
    private Industry industry;
    @ManyToOne
    private Employer employer;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications;
    private Double latitude;
    private Double longitude;

    // Status management (new fields)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING;

   // @ManyToOne
   // @JoinColumn(name = "approved_by_id")
   // private User approvedBy;

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

    // Helper methods
    public boolean isVisible() {
        return this.status == JobStatus.APPROVED;
    }

    public void approve(){//User approvedBy) {
        this.status = JobStatus.APPROVED;
      //  this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        this.status = JobStatus.REJECTED;
        this.rejectionReason = reason;
    //    this.approvedBy = null;
        this.approvedAt = null;
    }
}