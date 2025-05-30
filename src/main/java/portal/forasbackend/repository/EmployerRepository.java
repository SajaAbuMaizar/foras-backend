package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portal.forasbackend.entity.Employer;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    boolean existsByPhone(String phone);
    Optional<Employer> findByPhone(String phone);
}
