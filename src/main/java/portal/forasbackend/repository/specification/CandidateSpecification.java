package portal.forasbackend.repository.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import portal.forasbackend.dto.request.admin.CandidateFilterRequest;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.enums.Gender;

import java.util.ArrayList;
import java.util.List;

public class CandidateSpecification {

    private CandidateSpecification() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Candidate> withFilters(CandidateFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Gender filter
            if (request.getGenders() != null && !request.getGenders().isEmpty()) {
                predicates.add(root.get("gender").in(request.getGenders()));
            }

            // City filter
            if (request.getCities() != null && !request.getCities().isEmpty()) {
                predicates.add(root.get("city").get("code").in(request.getCities()));
            }

            // Hebrew knowledge filter
            if (request.getKnowsHebrew() != null) {
                predicates.add(criteriaBuilder.equal(root.get("knowsHebrew"), request.getKnowsHebrew()));
            }

            // Needs help filter
            if (request.getNeedsHelp() != null) {
                predicates.add(criteriaBuilder.equal(root.get("needsHelp"), request.getNeedsHelp()));
            }

            // Skills filter - candidates must have ALL specified skills
            if (request.getSkills() != null && !request.getSkills().isEmpty()) {
                for (String skill : request.getSkills()) {
                    predicates.add(criteriaBuilder.isMember(skill, root.get("skills")));
                }
            }

            // Driver licenses filter - candidates must have ALL specified licenses
            if (request.getDriverLicenses() != null && !request.getDriverLicenses().isEmpty()) {
                for (String license : request.getDriverLicenses()) {
                    predicates.add(criteriaBuilder.isMember(license, root.get("driverLicenses")));
                }
            }

            // Languages filter - candidates must have ALL specified languages
            if (request.getLanguages() != null && !request.getLanguages().isEmpty()) {
                for (String language : request.getLanguages()) {
                    predicates.add(criteriaBuilder.isMember(language, root.get("languages")));
                }
            }

            // Search query filter (name or phone)
            if (request.getSearchQuery() != null && !request.getSearchQuery().trim().isEmpty()) {
                String searchPattern = "%" + request.getSearchQuery().trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate phonePredicate = criteriaBuilder.like(
                        root.get("phone"), "%" + request.getSearchQuery().trim() + "%");

                predicates.add(criteriaBuilder.or(namePredicate, phonePredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Candidate> hasAnySkill(List<String> skills) {
        return (root, query, criteriaBuilder) -> {
            if (skills == null || skills.isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true
            }

            List<Predicate> skillPredicates = new ArrayList<>();
            for (String skill : skills) {
                skillPredicates.add(criteriaBuilder.isMember(skill, root.get("skills")));
            }
            return criteriaBuilder.or(skillPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Candidate> hasAnyLicense(List<String> licenses) {
        return (root, query, criteriaBuilder) -> {
            if (licenses == null || licenses.isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true
            }

            List<Predicate> licensePredicates = new ArrayList<>();
            for (String license : licenses) {
                licensePredicates.add(criteriaBuilder.isMember(license, root.get("driverLicenses")));
            }
            return criteriaBuilder.or(licensePredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Candidate> hasAnyLanguage(List<String> languages) {
        return (root, query, criteriaBuilder) -> {
            if (languages == null || languages.isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true
            }

            List<Predicate> languagePredicates = new ArrayList<>();
            for (String language : languages) {
                languagePredicates.add(criteriaBuilder.isMember(language, root.get("languages")));
            }
            return criteriaBuilder.or(languagePredicates.toArray(new Predicate[0]));
        };
    }
}