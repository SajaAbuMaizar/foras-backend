package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobApplication;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.service.JobApplicationService;
import portal.forasbackend.service.JobService;
import portal.forasbackend.service.JobTranslationService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        System.out.println( jobId);
        System.out.println( candidate);
        Long candidateId = candidate.getId();
        jobApplicationService.applyToJob(jobId, candidateId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public List<JobApplication> myApplications(@AuthenticationPrincipal Candidate candidate) {
        Long candidateId = candidate.getId();
        return jobApplicationService.getApplicationsByCandidate(candidateId);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> applicationsForJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal Employer employer) {

        Job job = jobApplicationService.getJobById(jobId);

        if (!job.getEmployer().getId().equals(employer.getId())) {
            return ResponseEntity.status(403).body("You are not authorized to view applications for this job.");
        }

        List<JobApplication> applications = jobApplicationService.getApplicationsForJob(jobId);
        // Extract only the candidates from the applications
        List<Candidate> candidates = applications.stream()
                .map(JobApplication::getCandidate)
                .toList();

        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/my-applied-jobs")
    public ResponseEntity<List<MainPageJobListResponse>> getAppliedJobsForCandidate(
            @AuthenticationPrincipal Candidate user
    ) {
        // 1. Find candidate by user id
        Candidate candidate = jobApplicationService.findCandidateByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // 2. Find all job applications for candidate
        List<JobApplication> applications = jobApplicationService.findByCandidate(candidate);

        // 3. Convert jobs to DTOs for frontend
        List<MainPageJobListResponse> jobs = applications.stream()
                .map(job -> jobTranslationService.getArabicTranslation(job.getId())
                        .map(t -> jobMapper.toDto(job.getJob(), t))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(jobs);
    }

}
