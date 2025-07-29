package portal.forasbackend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.domain.model.JobApplication;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findJobApplicationByJobId(Long jobId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<JobApplication> findByCandidateId(Long candidateId);
    List<JobApplication> findByJobId(Long jobId);

    List<JobApplication> findByCandidate(Candidate candidate);

}
