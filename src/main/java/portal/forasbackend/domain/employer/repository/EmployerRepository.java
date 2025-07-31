package portal.forasbackend.domain.employer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import portal.forasbackend.domain.employer.model.Employer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    boolean existsByPhone(String phone);
    Optional<Employer> findByPhone(String phone);

    // Dashboard methods
    long countByCreatedAtAfter(LocalDateTime after);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByLastLoginAfter(LocalDateTime after);

    Optional<Employer> findByCompanyName(String companyName);

    // Top cities with employer count (city name, count)
//    @Query(value = "SELECT c.name, COUNT(e.id) FROM employers e JOIN city c ON e.city_id = c.id GROUP BY c.name ORDER BY COUNT(e.id) DESC LIMIT :limit", nativeQuery = true)
//    List<Object[]> findTopCitiesWithEmployerCount(@org.springframework.data.repository.query.Param("limit") int limit);
}