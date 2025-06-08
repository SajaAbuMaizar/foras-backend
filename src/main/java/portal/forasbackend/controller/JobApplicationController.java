package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Job;
import portal.forasbackend.entity.JobApplication;
import portal.forasbackend.service.EmployerService;
import portal.forasbackend.service.JobApplicationService;
import portal.forasbackend.service.JobService;

import java.io.Console;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobService jobService;

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

}
