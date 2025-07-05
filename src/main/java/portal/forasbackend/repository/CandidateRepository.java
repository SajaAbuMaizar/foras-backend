package portal.forasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.enums.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    boolean existsByPhone(String phoneNumber);
    Optional<Candidate> findByPhone(String phone);

    @Query("SELECT DISTINCT skill FROM Candidate c JOIN c.skills skill ORDER BY skill")
    List<String> findAllUniqueSkills();

    @Query("SELECT DISTINCT license FROM Candidate c JOIN c.driverLicenses license ORDER BY license")
    List<String> findAllUniqueDriverLicenses();

    @Query("SELECT DISTINCT language FROM Candidate c JOIN c.languages language ORDER BY language")
    List<String> findAllUniqueLanguages();

    @Query("SELECT c FROM Candidate c JOIN c.skills s WHERE s IN :skills")
    org.springframework.data.domain.Page<Candidate> findBySkillsIn(List<String> skills, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT c FROM Candidate c JOIN c.driverLicenses dl WHERE dl IN :licenses")
    org.springframework.data.domain.Page<Candidate> findByDriverLicensesIn(List<String> licenses, org.springframework.data.domain.Pageable pageable);

    // Implemented methods for dashboard
    long countByCreatedAtAfter(LocalDateTime after);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByKnowsHebrew(boolean knowsHebrew);
    long countByGender(Gender gender);

    @Query("SELECT COUNT(c) FROM Candidate c WHERE SIZE(c.skills) > 0")
    long countBySkillsIsNotEmpty();

    long countByNeedsHelp(boolean needsHelp);

    long countByLastLoginAfter(LocalDateTime after);
}