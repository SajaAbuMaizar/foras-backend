package portal.forasbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String code;
    private String nameAr;
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