package portal.forasbackend.service.Admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portal.forasbackend.dto.request.admin.CandidateFilterRequest;
import portal.forasbackend.dto.response.admin.CandidateFilterResponse;
import portal.forasbackend.dto.response.admin.FilterOptionsResponse;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.City;
import portal.forasbackend.enums.Gender;
import portal.forasbackend.domain.repository.CandidateRepository;
import portal.forasbackend.domain.repository.CityRepository;
import portal.forasbackend.domain.repository.specification.CandidateSpecification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateFilterService {

    private final CandidateRepository candidateRepository;
    private final CityRepository cityRepository;

    public Page<CandidateFilterResponse> filterCandidates(CandidateFilterRequest request) {
        try {
            log.info("Filtering candidates with request: {}", request);

            // Create pageable with sorting
            Sort sort = createSort(request.getSortBy(), request.getSortDirection());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

            // Apply filters using specification
            Page<Candidate> candidates = candidateRepository.findAll(
                    CandidateSpecification.withFilters(request), pageable);

            // Convert to response DTOs
            Page<CandidateFilterResponse> response = candidates.map(this::convertToFilterResponse);

            log.info("Found {} candidates matching filter criteria", response.getTotalElements());
            return response;

        } catch (Exception e) {
            log.error("Error filtering candidates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to filter candidates: " + e.getMessage());
        }
    }

    public FilterOptionsResponse getFilterOptions() {
        try {
            log.info("Fetching filter options");

            // Get all available options from the database
            List<String> availableSkills = candidateRepository.findAllUniqueSkills();
            List<String> availableDriverLicenses = candidateRepository.findAllUniqueDriverLicenses();
            List<String> availableLanguages = candidateRepository.findAllUniqueLanguages();

            // Get cities
            Map<String, String> availableCities = cityRepository.findAll().stream()
                    .collect(Collectors.toMap(City::getCode, City::getNameAr));

            return FilterOptionsResponse.builder()
                    .availableGenders(Arrays.asList(Gender.values()))
                    .availableSkills(availableSkills)
                    .availableDriverLicenses(availableDriverLicenses)
                    .availableLanguages(availableLanguages)
                    .availableCities(availableCities)
                    .build();

        } catch (Exception e) {
            log.error("Error fetching filter options: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch filter options: " + e.getMessage());
        }
    }

    public List<CandidateFilterResponse> findCandidatesWithAnySkill(List<String> skills) {
        try {
            List<Candidate> candidates = candidateRepository.findAll(
                    CandidateSpecification.hasAnySkill(skills));

            return candidates.stream()
                    .map(this::convertToFilterResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error finding candidates with skills {}: {}", skills, e.getMessage(), e);
            throw new RuntimeException("Failed to find candidates with specified skills");
        }
    }

    public List<CandidateFilterResponse> findCandidatesWithAnyLicense(List<String> licenses) {
        try {
            List<Candidate> candidates = candidateRepository.findAll(
                    CandidateSpecification.hasAnyLicense(licenses));

            return candidates.stream()
                    .map(this::convertToFilterResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error finding candidates with licenses {}: {}", licenses, e.getMessage(), e);
            throw new RuntimeException("Failed to find candidates with specified licenses");
        }
    }

    private CandidateFilterResponse convertToFilterResponse(Candidate candidate) {
        return CandidateFilterResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .phone(candidate.getPhone())
                .cityName(candidate.getCity().getNameAr())
                .gender(candidate.getGender())
                .knowsHebrew(candidate.isKnowsHebrew())
                .needsHelp(candidate.isNeedsHelp())
                .skills(candidate.getSkills() != null ? candidate.getSkills() : List.of())
                .driverLicenses(candidate.getDriverLicenses() != null ? candidate.getDriverLicenses() : List.of())
                .languages(candidate.getLanguages() != null ? candidate.getLanguages() : List.of())
                .avatarUrl(candidate.getAvatarUrl())
                .build();
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return switch (sortBy.toLowerCase()) {
            case "phone" -> Sort.by(direction, "phone");
            case "city" -> Sort.by(direction, "city.nameAr");
            case "gender" -> Sort.by(direction, "gender");
            default -> Sort.by(direction, "name");
        };
    }
}