package portal.forasbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.application.dto.response.job.JobApplicationResponse;
import portal.forasbackend.domain.model.ApplicationStatus;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.JobApplication;
import portal.forasbackend.domain.model.Job;
import portal.forasbackend.core.exception.ResourceNotFoundException;
import portal.forasbackend.core.exception.UnauthorizedException;
import portal.forasbackend.application.mapper.JobApplicationMapper;
import portal.forasbackend.domain.repository.CandidateRepository;
import portal.forasbackend.domain.repository.JobApplicationRepository;
import portal.forasbackend.domain.repository.JobRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepo;
    private final JobRepository jobRepo;
    private final CandidateRepository candidateRepo;
    private final JobApplicationMapper jobApplicationMapper;

    @Transactional
    public void applyToJob(Long jobId, Long candidateId) {
        // Check if already applied first (this is a quick read operation)
        if (hasAlreadyApplied(jobId, candidateId)) {
            throw new IllegalStateException("You have already applied to this job");
        }

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .status(ApplicationStatus.PENDING) // Set default status
                .build();

        try {
            jobApplicationRepo.save(application);
            log.info("Successfully created job application for candidate {} to job {}", candidateId, jobId);
        } catch (DataIntegrityViolationException e) {
            // This handles cases where a concurrent request created the application
            log.warn("Duplicate application attempt for candidate {} to job {}", candidateId, jobId);
            throw new IllegalStateException("You have already applied to this job");
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

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsForJob(Long jobId, Long employerId) {
        // Verify the job belongs to the employer
        if (!jobRepo.existsByIdAndEmployerId(jobId, employerId)) {
            throw new UnauthorizedException("You don't have access to this job's applications");
        }

        List<JobApplication> applications = jobApplicationRepo.findByJobId(jobId);

        return applications.stream()
                .map(jobApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId, ApplicationStatus status, Long employerId) {
        JobApplication application = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Verify the application's job belongs to the employer
        if (!application.getJob().getEmployer().getId().equals(employerId)) {
            throw new UnauthorizedException("You don't have access to this application");
        }

        application.setStatus(status);
        jobApplicationRepo.save(application);
    }
}