package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.SavedJob;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);

    Optional<SavedJob> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    List<SavedJob> findByCandidateIdOrderBySavedAtDesc(Long candidateId);

    @Query("SELECT sj FROM SavedJob sj " +
            "JOIN FETCH sj.job j " +
            "JOIN FETCH j.employer " +
            "JOIN FETCH j.city " +
            "JOIN FETCH j.industry " +
            "WHERE sj.candidate.id = :candidateId " +
            "ORDER BY sj.savedAt DESC")
    List<SavedJob> findByCandidateIdWithJobDetails(@Param("candidateId") Long candidateId);

    void deleteByJobIdAndCandidateId(Long jobId, Long candidateId);
}