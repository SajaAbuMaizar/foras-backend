package portal.forasbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private String language; // "he" or "ar"

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private String requiredQualifications;

    private boolean isOriginal;

//    private boolean isVerified;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "verified_by_id")
//    private Admin verifiedBy;
//
//    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}