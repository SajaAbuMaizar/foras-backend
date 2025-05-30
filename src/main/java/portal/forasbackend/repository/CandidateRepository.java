package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.entity.Candidate;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsByPhone(String phoneNumber);
    Optional<Candidate> findByPhone(String phone);

}