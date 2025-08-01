package portal.forasbackend.domain.model;

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
public class Industry implements LocalizableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

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