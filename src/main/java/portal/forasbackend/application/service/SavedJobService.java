package portal.forasbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.application.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.Job;
import portal.forasbackend.domain.model.SavedJob;
import portal.forasbackend.application.mapper.JobMapper;
import portal.forasbackend.domain.repository.CandidateRepository;
import portal.forasbackend.domain.repository.JobRepository;
import portal.forasbackend.domain.repository.SavedJobRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final JobTranslationService jobTranslationService;
    private final JobMapper jobMapper;

    @Transactional
    public void saveJob(Long jobId, Long candidateId) {
        try {
            // Check if already saved
            if (isJobSaved(jobId, candidateId)) {
                throw new IllegalStateException("Job is already saved");
            }

            Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

            // Check if job is visible/approved
            if (!job.isVisible()) {
                throw new IllegalStateException("This job is not available");
            }

            Candidate candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

            SavedJob savedJob = SavedJob.builder()
                    .job(job)
                    .candidate(candidate)
                    .build();

            savedJobRepository.save(savedJob);
            log.info("Successfully saved job {} for candidate {}", jobId, candidateId);

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate save attempt for candidate {} to job {}", candidateId, jobId);
            throw new IllegalStateException("Job is already saved");
        } catch (Exception e) {
            log.error("Error saving job {} for candidate {}: {}", jobId, candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to save job: " + e.getMessage());
        }
    }

    @Transactional
    public void unsaveJob(Long jobId, Long candidateId) {
        try {
            savedJobRepository.deleteByJobIdAndCandidateId(jobId, candidateId);
            log.info("Successfully unsaved job {} for candidate {}", jobId, candidateId);
        } catch (Exception e) {
            log.error("Error unsaving job {} for candidate {}: {}", jobId, candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to unsave job: " + e.getMessage());
        }
    }

    public boolean isJobSaved(Long jobId, Long candidateId) {
        return savedJobRepository.existsByJobIdAndCandidateId(jobId, candidateId);
    }

    public List<MainPageJobListResponse> getSavedJobsForCandidate(Long candidateId) {
        try {
            List<SavedJob> savedJobs = savedJobRepository.findByCandidateIdWithJobDetails(candidateId);

            return savedJobs.stream()
                    .map(savedJob -> jobTranslationService.getArabicTranslation(savedJob.getJob().getId())
                            .map(translation -> jobMapper.toDto(savedJob.getJob(), translation))
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching saved jobs for candidate {}: {}", candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch saved jobs: " + e.getMessage());
        }
    }
}