package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Double latitude;
    private Double longitude;
}
