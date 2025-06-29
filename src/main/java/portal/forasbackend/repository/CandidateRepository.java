package portal.forasbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import portal.forasbackend.entity.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    boolean existsByPhone(String phoneNumber);
    Optional<Candidate> findByPhone(String phone);

    // Get all unique skills from all candidates
    @Query("SELECT DISTINCT skill FROM Candidate c JOIN c.skills skill ORDER BY skill")
    List<String> findAllUniqueSkills();

    // Get all unique driver licenses from all candidates
    @Query("SELECT DISTINCT license FROM Candidate c JOIN c.driverLicenses license ORDER BY license")
    List<String> findAllUniqueDriverLicenses();

    // Get all unique languages from all candidates
    @Query("SELECT DISTINCT language FROM Candidate c JOIN c.languages language ORDER BY language")
    List<String> findAllUniqueLanguages();

    // Find candidates with specific skills (for analytics)
    @Query("SELECT c FROM Candidate c JOIN c.skills s WHERE s IN :skills")
    Page<Candidate> findBySkillsIn(List<String> skills, Pageable pageable);

    // Find candidates with specific licenses (for analytics)
    @Query("SELECT c FROM Candidate c JOIN c.driverLicenses dl WHERE dl IN :licenses")
    Page<Candidate> findByDriverLicensesIn(List<String> licenses, Pageable pageable);
}