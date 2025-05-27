package portal.forasbackend.enums;

public enum Gender {
    MALE("ذكر", "זכר"),
    FEMALE("أنثى", "נקבה");

    private final String ar;
    private final String he;

    Gender(String ar, String he) {
        this.ar = ar;
        this.he = he;
    }

    public String getArabic() {
        return ar;
    }

    public String getHebrew() {
        return he;
    }
    
}
