package portal.forasbackend.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.domain.admin.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByPhone(String phone);
}
