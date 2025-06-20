package portal.forasbackend.enums;

import lombok.Getter;

@Getter
public enum JobStatus {
    PENDING("Pending", "bg-yellow-100"),
    APPROVED("Approved", "bg-green-100"),
    REJECTED("Rejected", "bg-red-100"),
    EXPIRED("Expired", "bg-gray-100"),
    DRAFT("Draft", "bg-blue-100");

    // Getters
    private final String displayName;
    private final String tailwindColor;

    JobStatus(String displayName, String tailwindColor) {
        this.displayName = displayName;
        this.tailwindColor = tailwindColor;
    }

    // Additional methods
    public boolean isActive() {
        return this == PENDING || this == APPROVED;
    }

}