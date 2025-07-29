package portal.forasbackend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.domain.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByPhone(String phone);
}
