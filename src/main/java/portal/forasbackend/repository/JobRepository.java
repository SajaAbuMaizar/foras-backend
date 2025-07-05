package portal.forasbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portal.forasbackend.entity.Job;
import portal.forasbackend.enums.JobStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
// fixme do the job repo calude
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findByEmployerId(Long employerId);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.applications a LEFT JOIN FETCH a.candidate WHERE j.id = :id")
    Optional<Job> findByIdWithCandidates(@Param("id") Long id);

    @Query("""
    SELECT j FROM Job j
    LEFT JOIN FETCH j.translations
    LEFT JOIN FETCH j.city
    LEFT JOIN FETCH j.industry
    WHERE j.id = :id
""")
    Optional<Job> findByIdWithDetails(@Param("id") Long id);

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.translations WHERE j.id = :id")
    Optional<Job> findByIdWithTranslations(@Param("id") Long id);

    List<Job> findAllByOrderByCreatedAtDesc();

    List<Job> findByEmployerIdOrderByCreatedAtDesc(Long employerId);

    boolean existsByIdAndEmployerId(Long id, Long employerId);

}
