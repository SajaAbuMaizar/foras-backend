package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {}
