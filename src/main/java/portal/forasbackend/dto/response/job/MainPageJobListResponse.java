package portal.forasbackend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainPageJobListResponse {
    private Long id;
    private String imageUrl;
    private String salary;
    private String jobTitle;
    private EmployerDto employer;
    private String jobDescription;
    private String requiredQualifications;
    private boolean hebrewRequired;
    private boolean transportationAvailable;
    private String jobTypeName;
    private String cityName;
    private String industryName;
    private Double latitude;
    private Double longitude;
    private String publishDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployerDto {
        private Long id;
        private String companyName;
        private String companyLogoUrl;
    }
}