package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.UpdateStatusRequest;
import portal.forasbackend.dto.response.job.JobApplicationResponse;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.domain.candidate.model.Candidate;
import portal.forasbackend.domain.employer.model.Employer;
import portal.forasbackend.domain.job.model.Job;
import portal.forasbackend.domain.application.model.JobApplication;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.domain.application.service.JobApplicationService;
import portal.forasbackend.domain.job.service.JobService;
import portal.forasbackend.domain.job.service.JobTranslationService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobService jobService;
    private final JobMapper jobMapper;
    private final JobTranslationService jobTranslationService;

    @PostMapping("/{jobId}")
    public ResponseEntity<?> applyToJob(@PathVariable Long jobId, @AuthenticationPrincipal Candidate candidate) {
        try {
            log.info("Candidate {} applying to job {}", candidate.getId(), jobId);

            // Validate inputs
            if (jobId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Job ID is required"));
            }

            if (candidate == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Authentication required"));
            }

            // Check if job exists and is approved
            Job job = jobService.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            if (!job.isVisible()) {
                return ResponseEntity.badRequest().body(Map.of("message", "This job is not available for applications"));
            }

            jobApplicationService.applyToJob(jobId, candidate.getId());

            log.info("Successfully applied candidate {} to job {}", candidate.getId(), jobId);
            return ResponseEntity.ok().body(Map.of("message", "Application submitted successfully"));

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate application attempt for candidate {} to job {}",
                    candidate != null ? candidate.getId() : "null", jobId);
            return ResponseEntity.status(409).body(Map.of("message", "You have already applied to this job"));
        } catch (IllegalStateException e) {
            log.warn("Application conflict for candidate {} and job {}: {}",
                    candidate != null ? candidate.getId() : "null", jobId, e.getMessage());
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error applying to job {} for candidate {}: {}",
                    jobId, candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message", "Failed to submit application. Please try again."));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<JobApplication>> myApplications(@AuthenticationPrincipal Candidate candidate) {
        try {
            if (candidate == null) {
                return ResponseEntity.status(401).build();
            }

            Long candidateId = candidate.getId();
            List<JobApplication> applications = jobApplicationService.getApplicationsByCandidate(candidateId);
            return ResponseEntity.ok(applications);

        } catch (Exception e) {
            log.error("Error fetching applications for candidate {}: {}",
                    candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> applicationsForJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal Employer employer) {
        try {
            if (employer == null) {
                return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));
            }

            Job job = jobApplicationService.getJobById(jobId);

            if (!job.getEmployer().getId().equals(employer.getId())) {
                return ResponseEntity.status(403).body(Map.of("message", "You are not authorized to view applications for this job."));
            }

            List<JobApplication> applications = jobApplicationService.getApplicationsForJob(jobId);
            // Extract only the candidates from the applications
            List<Candidate> candidates = applications.stream()
                    .map(JobApplication::getCandidate)
                    .toList();

            return ResponseEntity.ok(candidates);

        } catch (Exception e) {
            log.error("Error fetching applications for job {}: {}", jobId, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch applications"));
        }
    }

    @GetMapping("/my-applied-jobs")
    public ResponseEntity<List<MainPageJobListResponse>> getAppliedJobsForCandidate(
            @AuthenticationPrincipal Candidate candidate
    ) {
        System.out.println("Fetching applied jobs for candidate: " + (candidate != null ? candidate.getId() : "null"));
        try {
            if (candidate == null) {
                return ResponseEntity.status(401).build();
            }
            System.out.println("Candidate ID: " + candidate.getId());

            // 1. Find candidate by user id
            Candidate foundCandidate = jobApplicationService.findCandidateByUserId(candidate.getId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            System.out.println("Found candidate: " + foundCandidate.getId());

            // 2. Find all job applications for candidate
            List<JobApplication> applications = jobApplicationService.findByCandidate(foundCandidate);

            System.out.println("Found " + applications.size() + " applications for candidate " + foundCandidate.getId());

            // 3. Convert jobs to DTOs for frontend
            List<MainPageJobListResponse> jobs = applications.stream()
                    .map(application -> jobTranslationService.getArabicTranslation(application.getJob().getId())
                            .map(t -> jobMapper.toDto(application.getJob(), t))
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .toList();

            System.out.println("Converted applications to DTOs, total jobs: " + jobs.size());

            return ResponseEntity.ok(jobs);

        } catch (Exception e) {
            log.error("Error fetching applied jobs for candidate {}: {}",
                    candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

   // @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/{jobId}/applications")
    public ResponseEntity<List<JobApplicationResponse>> getJobApplications(
            @PathVariable Long jobId,
            @AuthenticationPrincipal Employer employer) {

        List<JobApplicationResponse> applications =
                jobApplicationService.getApplicationsForJob(jobId, employer.getId());

        return ResponseEntity.ok(applications);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateStatusRequest request,
            @AuthenticationPrincipal Employer employer) {

        jobApplicationService.updateApplicationStatus(
                applicationId,
                request.getStatus(),
                employer.getId()
        );

        return ResponseEntity.ok().build();
    }
}