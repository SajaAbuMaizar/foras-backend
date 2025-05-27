package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phoneNumber);

}