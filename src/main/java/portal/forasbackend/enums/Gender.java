package portal.forasbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum Gender {
    MALE("ذكر", "זכר", "1"),
    FEMALE("أنثى", "נקבה" , "2");

    private final String ar;
    private final String he;
    @Getter
    private final String id;

    Gender(String ar, String he, String id) {
        this.ar = ar;
        this.he = he;
        this.id = id;
    }

    public String getArabic() {
        return ar;
    }

    public String getHebrew() {
        return he;
    }

}
