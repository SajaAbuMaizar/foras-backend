package portal.forasbackend.service.Admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.dto.response.admin.DashboardStatsResponse;
import portal.forasbackend.dto.response.admin.ActivityResponse;
import portal.forasbackend.dto.response.admin.CityCountDto;
import portal.forasbackend.enums.Gender;
import portal.forasbackend.enums.JobStatus;
import portal.forasbackend.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private final CandidateRepository candidateRepository;
    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final MessageRepository messageRepository;
    private final ActivityLogRepository activityLogRepository;

    public DashboardStatsResponse getDashboardStats() {
        log.info("Fetching dashboard statistics");

        // Candidates stats
        long totalCandidates = candidateRepository.count();
        long candidatesLastMonth = candidateRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusMonths(1));
        long candidatesPreviousMonth = candidateRepository.countByCreatedAtBetween(
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMonths(1));

        double candidatesChange = calculatePercentageChange(
                candidatesPreviousMonth, candidatesLastMonth);

        long candidatesKnowHebrew = candidateRepository.countByKnowsHebrew(true);
        long candidatesNoHebrew = candidateRepository.countByKnowsHebrew(false);
        long candidatesMale = candidateRepository.countByGender(Gender.MALE);
        long candidatesFemale = candidateRepository.countByGender(Gender.FEMALE);
        long candidatesWithSkills = candidateRepository.countBySkillsIsNotEmpty();
        long candidatesNeedHelp = candidateRepository.countByNeedsHelp(true);

        // Employers stats
        long totalEmployers = employerRepository.count();
        long employersLastMonth = employerRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusMonths(1));
        long employersPreviousMonth = employerRepository.countByCreatedAtBetween(
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMonths(1));

        double employersChange = calculatePercentageChange(
                employersPreviousMonth, employersLastMonth);

        long activeEmployers = employerRepository.countByLastLoginAfter(
                LocalDateTime.now().minusDays(30));
        long inactiveEmployers = totalEmployers - activeEmployers;

//        List<CityCountDto> topEmployerCities = getTopEmployerCities(5);
//        double avgJobsPerEmployer = totalEmployers > 0 ?
//                (double) jobRepository.count() / totalEmployers : 0;

        // Jobs stats
        long totalJobs = jobRepository.count();
        long jobsLastMonth = jobRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusMonths(1));
        long jobsPreviousMonth = jobRepository.countByCreatedAtBetween(
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMonths(1));

        double jobsChange = calculatePercentageChange(
                jobsPreviousMonth, jobsLastMonth);

        long approvedJobs = jobRepository.countByStatus(JobStatus.APPROVED);
        long pendingJobs = jobRepository.countByStatus(JobStatus.PENDING);
        long rejectedJobs = jobRepository.countByStatus(JobStatus.REJECTED);

        // Messages stats
        long totalMessages = messageRepository.count();
        long messagesLastMonth = messageRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusMonths(1));
        long messagesPreviousMonth = messageRepository.countByCreatedAtBetween(
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMonths(1));

        double messagesChange = calculatePercentageChange(
                messagesPreviousMonth, messagesLastMonth);

        long newMessages = messageRepository.countByReadFalse();
        long sentMessages = messageRepository.countBySentTrue();
        long archivedMessages = messageRepository.countByArchivedTrue();
        long deletedMessages = messageRepository.countByDeletedTrue();

        double messageResponseRate = calculateMessageResponseRate();

        // Active users
        long activeUsers = candidateRepository.countByLastLoginAfter(
                LocalDateTime.now().minusDays(1)) +
                employerRepository.countByLastLoginAfter(
                        LocalDateTime.now().minusDays(1));

        return DashboardStatsResponse.builder()
                // Candidates
                .totalCandidates(totalCandidates)
                .candidatesChange(candidatesChange)
                .candidatesKnowHebrew(candidatesKnowHebrew)
                .candidatesNoHebrew(candidatesNoHebrew)
                .candidatesMale(candidatesMale)
                .candidatesFemale(candidatesFemale)
                .candidatesWithSkills(candidatesWithSkills)
                .candidatesNeedHelp(candidatesNeedHelp)
                // Employers
                .totalEmployers(totalEmployers)
                .employersChange(employersChange)
                .activeEmployers(activeEmployers)
                .inactiveEmployers(inactiveEmployers)
//                .topEmployerCities(topEmployerCities)
//                .avgJobsPerEmployer(avgJobsPerEmployer)
                // Jobs
                .totalJobs(totalJobs)
                .jobsChange(jobsChange)
                .approvedJobs(approvedJobs)
                .pendingJobs(pendingJobs)
                .rejectedJobs(rejectedJobs)
                // Messages
                .totalMessages(totalMessages)
                .messagesChange(messagesChange)
                .newMessages(newMessages)
                .sentMessages(sentMessages)
                .archivedMessages(archivedMessages)
                .deletedMessages(deletedMessages)
                .messageResponseRate(messageResponseRate)
                // Activity
                .activeUsers(activeUsers)
                .lastUpdated(LocalDateTime.now().toString())
                .build();
    }

    public List<ActivityResponse> getRecentActivities(int limit) {
        log.info("Fetching recent {} activities", limit);

        return activityLogRepository.findTopNByOrderByCreatedAtDesc(Pageable.ofSize(limit)).stream()
                .map(activity -> ActivityResponse.builder()
                        .id(activity.getId().toString())
                        .type(activity.getActivityType())
                        .description(activity.getDescription())
                        .createdAt(activity.getCreatedAt().toString())
                        .userId(activity.getUserId() != null ? activity.getUserId().toString() : null)
                        .userName(activity.getUserName())
                        .build())
                .collect(Collectors.toList());
    }

    private double calculatePercentageChange(long previous, long current) {
        if (previous == 0) return current > 0 ? 100.0 : 0.0;
        return ((double) (current - previous) / previous) * 100;
    }

//    private List<CityCountDto> getTopEmployerCities(int limit) {
//        return employerRepository.findTopCitiesWithEmployerCount(limit).stream()
//                .map(result -> CityCountDto.builder()
//                        .name((String) result[0])
//                        .count(((Number) result[1]).longValue())
//                        .build())
//                .collect(Collectors.toList());
//    }

    private double calculateMessageResponseRate() {
        long totalSent = messageRepository.countBySentTrue();
        long totalResponded = messageRepository.countByRespondedTrue();
        return totalSent > 0 ? ((double) totalResponded / totalSent) * 100 : 0.0;
    }
}