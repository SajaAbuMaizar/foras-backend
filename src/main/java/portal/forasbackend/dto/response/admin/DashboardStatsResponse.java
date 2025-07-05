package portal.forasbackend.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    // Candidates
    private long totalCandidates;
    private double candidatesChange;
    private long candidatesKnowHebrew;
    private long candidatesNoHebrew;
    private long candidatesMale;
    private long candidatesFemale;
    private long candidatesWithSkills;
    private long candidatesNeedHelp;

    // Employers
    private long totalEmployers;
    private double employersChange;
    private long activeEmployers;
    private long inactiveEmployers;
    private List<CityCountDto> topEmployerCities;
    private double avgJobsPerEmployer;

    // Jobs
    private long totalJobs;
    private double jobsChange;
    private long approvedJobs;
    private long pendingJobs;
    private long rejectedJobs;

    // Messages
    private long totalMessages;
    private double messagesChange;
    private long newMessages;
    private long sentMessages;
    private long archivedMessages;
    private long deletedMessages;
    private double messageResponseRate;

    // Activity
    private long activeUsers;
    private String lastUpdated;
}