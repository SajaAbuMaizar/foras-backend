package portal.forasbackend.domain.job.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.common.model.LocalizableEntity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobType implements LocalizableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // e.g., FULL_TIME, PART_TIME

    @Column(nullable = false)
    private String nameAr;

    @Column(nullable = false)
    private String nameHe;

    @Override
    public String getNameAr() {
        return nameAr;
    }

    @Override
    public String getNameHe() {
        return nameHe;
    }
}