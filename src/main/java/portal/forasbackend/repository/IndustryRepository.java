package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portal.forasbackend.entity.Industry;

import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
    Optional<Industry> findByCode(String code);

}