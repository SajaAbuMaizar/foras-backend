package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.stereotype.Service;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.JobApplication;
import portal.forasbackend.entity.Job;
import portal.forasbackend.repository.CandidateRepository;
import portal.forasbackend.repository.JobApplicationRepository;
import portal.forasbackend.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepo;
    private final JobRepository jobRepo;
    private final CandidateRepository candidateRepo;

    public void applyToJob(Long jobId, Long candidateId) {
        if (jobApplicationRepo.existsByJobIdAndCandidateId(jobId, candidateId)) {
            throw new IllegalStateException("Already applied to this job");
        }

        Job job = jobRepo.findById(jobId).orElseThrow();
        Candidate candidate = candidateRepo.findById(candidateId).orElseThrow();

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .build();

        jobApplicationRepo.save(application);
    }

    public List<JobApplication> getApplicationsByCandidate(Long candidateId) {
        return jobApplicationRepo.findByCandidateId(candidateId);
    }

    public List<JobApplication> getApplicationsForJob(Long jobId) {
        return jobApplicationRepo.findByJobId(jobId);
    }

    public Job getJobById(Long jobId) {
        return jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public Optional<Candidate> findCandidateByUserId(Long userId) {
        return candidateRepo.findById(userId);
    }

    public List<JobApplication> findByCandidate(Candidate candidate) {
        return jobApplicationRepo.findByCandidate(candidate);
    }


}
