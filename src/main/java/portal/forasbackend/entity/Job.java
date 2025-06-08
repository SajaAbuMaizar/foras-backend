package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }

}
