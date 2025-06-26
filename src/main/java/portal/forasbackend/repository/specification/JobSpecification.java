package portal.forasbackend.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import portal.forasbackend.entity.Job;
import jakarta.persistence.criteria.Predicate;
import portal.forasbackend.enums.JobStatus;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    private JobSpecification() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Job> withFilters(
            String cityCode,
            String industryCode,
            Boolean hebrewRequired,
            Boolean transportationAvailable) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔒 Only approved jobs
            predicates.add(criteriaBuilder.equal(root.get("status"), JobStatus.APPROVED));

            if (cityCode != null && !cityCode.equals("all")) {
                predicates.add(criteriaBuilder.equal(
                        root.get("city").get("code"),
                        cityCode
                ));
            }

            if (industryCode != null && !industryCode.equals("all")) {
                predicates.add(criteriaBuilder.equal(
                        root.get("industry").get("code"),
                        industryCode
                ));
            }

            if (hebrewRequired != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("hebrewRequired"),
                        hebrewRequired
                ));
            }

            if (transportationAvailable != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("transportationAvailable"),
                        transportationAvailable
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}