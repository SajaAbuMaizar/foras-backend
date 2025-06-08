package portal.forasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import portal.forasbackend.common.model.LocalizableEntity;

// LocalizedNameDto.java
@Data
@AllArgsConstructor
public class LocalizedNameDto {
    private String nameAr;
    private String nameHe;

    public static LocalizedNameDto from(LocalizableEntity entity) {
        return new LocalizedNameDto(entity.getNameAr(), entity.getNameHe());
    }
}
