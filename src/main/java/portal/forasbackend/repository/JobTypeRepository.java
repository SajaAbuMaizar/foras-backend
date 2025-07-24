package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portal.forasbackend.entity.JobType;

import java.util.Optional;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {

    Optional<JobType> findByCode(String code);

    boolean existsByCode(String code);
}