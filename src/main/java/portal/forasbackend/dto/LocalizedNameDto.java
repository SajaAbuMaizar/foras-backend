package portal.forasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.common.model.LocalizableEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedNameDto {
    private String nameAr;
    private String nameHe;

    public static LocalizedNameDto from(LocalizableEntity entity) {
        if (entity == null) return null; // ✅ Prevent NullPointerException
        return new LocalizedNameDto(entity.getNameAr(), entity.getNameHe());
    }
}
