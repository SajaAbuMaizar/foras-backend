package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.service.SavedJobService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/save-job/{jobId}")
    public ResponseEntity<?> saveJob(@PathVariable Long jobId, @AuthenticationPrincipal Candidate candidate) {
        try {
            log.info("Candidate {} saving job {}", candidate.getId(), jobId);

            if (jobId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Job ID is required"));
            }

            if (candidate == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Authentication required"));
            }

            // Check if already saved
            if (savedJobService.isJobSaved(jobId, candidate.getId())) {
                return ResponseEntity.status(409).body(Map.of("message", "Job is already saved"));
            }

            savedJobService.saveJob(jobId, candidate.getId());

            log.info("Successfully saved job {} for candidate {}", jobId, candidate.getId());
            return ResponseEntity.ok().body(Map.of("message", "Job saved successfully"));

        } catch (IllegalStateException e) {
            log.warn("Save job conflict for candidate {} and job {}: {}",
                    candidate != null ? candidate.getId() : "null", jobId, e.getMessage());
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error saving job {} for candidate {}: {}",
                    jobId, candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message", "Failed to save job. Please try again."));
        }
    }

    @DeleteMapping("/unsave-job/{jobId}")
    public ResponseEntity<?> unsaveJob(@PathVariable Long jobId, @AuthenticationPrincipal Candidate candidate) {
        try {
            log.info("Candidate {} unsaving job {}", candidate.getId(), jobId);

            if (jobId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Job ID is required"));
            }

            if (candidate == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Authentication required"));
            }

            savedJobService.unsaveJob(jobId, candidate.getId());

            log.info("Successfully unsaved job {} for candidate {}", jobId, candidate.getId());
            return ResponseEntity.ok().body(Map.of("message", "Job removed from saved list"));

        } catch (Exception e) {
            log.error("Error unsaving job {} for candidate {}: {}",
                    jobId, candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message", "Failed to remove job from saved list"));
        }
    }

    @GetMapping("/saved-jobs")
    public ResponseEntity<List<MainPageJobListResponse>> getSavedJobs(@AuthenticationPrincipal Candidate candidate) {
        try {
            if (candidate == null) {
                return ResponseEntity.status(401).build();
            }

            List<MainPageJobListResponse> savedJobs = savedJobService.getSavedJobsForCandidate(candidate.getId());
            return ResponseEntity.ok(savedJobs);

        } catch (Exception e) {
            log.error("Error fetching saved jobs for candidate {}: {}",
                    candidate != null ? candidate.getId() : "null", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}