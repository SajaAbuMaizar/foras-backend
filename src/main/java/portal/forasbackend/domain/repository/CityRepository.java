package portal.forasbackend.domain.repository;
import portal.forasbackend.domain.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByCode(String code);
}
