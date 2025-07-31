package portal.forasbackend.domain.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portal.forasbackend.domain.job.model.Job;
import portal.forasbackend.domain.job.model.JobTranslation;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobTranslationRepository extends JpaRepository<JobTranslation, Long> {
    List<JobTranslation> findByJobId(Long jobId);

    Optional<JobTranslation> findByJobIdAndLanguage(Long jobId, String language);

    Optional<JobTranslation> findByIdAndJobId(Long translationId, Long jobId);
}
