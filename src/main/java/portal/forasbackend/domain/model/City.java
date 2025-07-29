package portal.forasbackend.domain.model;
import jakarta.persistence.*;
import lombok.*;
import portal.forasbackend.core.common.model.LocalizableEntity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City implements LocalizableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // internal name, e.g. JERUSALEM

    @Column(nullable = false)
    private String nameAr; // Arabic name

    @Column(nullable = false)
    private String nameHe; // Hebrew name

    @Override
    public String getNameAr() {
        return nameAr;
    }

    @Override
    public String getNameHe() {
        return nameHe;
    }
}