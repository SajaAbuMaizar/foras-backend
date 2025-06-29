package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.JobApplication;
import portal.forasbackend.entity.Job;
import portal.forasbackend.repository.CandidateRepository;
import portal.forasbackend.repository.JobApplicationRepository;
import portal.forasbackend.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepo;
    private final JobRepository jobRepo;
    private final CandidateRepository candidateRepo;

    @Transactional
    public void applyToJob(Long jobId, Long candidateId) {
        try {
            // Check if already applied
            if (hasAlreadyApplied(jobId, candidateId)) {
                throw new IllegalStateException("Already applied to this job");
            }

            Job job = jobRepo.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

            Candidate candidate = candidateRepo.findById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

            JobApplication application = JobApplication.builder()
                    .job(job)
                    .candidate(candidate)
                    .build();

            jobApplicationRepo.save(application);
            log.info("Successfully created job application for candidate {} to job {}", candidateId, jobId);

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate application attempt for candidate {} to job {}", candidateId, jobId);
            throw new IllegalStateException("Already applied to this job");
        } catch (Exception e) {
            log.error("Error creating job application for candidate {} to job {}: {}", candidateId, jobId, e.getMessage(), e);
            throw new RuntimeException("Failed to submit application: " + e.getMessage());
        }
    }

    public boolean hasAlreadyApplied(Long jobId, Long candidateId) {
        return jobApplicationRepo.existsByJobIdAndCandidateId(jobId, candidateId);
    }

    public List<JobApplication> getApplicationsByCandidate(Long candidateId) {
        return jobApplicationRepo.findByCandidateId(candidateId);
    }

    public List<JobApplication> getApplicationsForJob(Long jobId) {
        return jobApplicationRepo.findByJobId(jobId);
    }

    public Job getJobById(Long jobId) {
        return jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
    }

    public Optional<Candidate> findCandidateByUserId(Long userId) {
        return candidateRepo.findById(userId);
    }

    public List<JobApplication> findByCandidate(Candidate candidate) {
        return jobApplicationRepo.findByCandidate(candidate);
    }
}