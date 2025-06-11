package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portal.forasbackend.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findByEmployerId(Long employerId);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.applications a LEFT JOIN FETCH a.candidate WHERE j.id = :id")
    Optional<Job> findByIdWithCandidates(@Param("id") Long id);


}
