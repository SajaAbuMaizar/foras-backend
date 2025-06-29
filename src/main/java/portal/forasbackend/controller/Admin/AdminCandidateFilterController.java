package portal.forasbackend.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.admin.CandidateFilterRequest;
import portal.forasbackend.dto.response.admin.CandidateFilterResponse;
import portal.forasbackend.dto.response.admin.FilterOptionsResponse;
import portal.forasbackend.dto.response.shared.PagedResponse;
import portal.forasbackend.service.Admin.CandidateFilterService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/candidates")
@RequiredArgsConstructor
@Tag(name = "Admin Candidate Filter", description = "Admin endpoints for filtering and searching candidates")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminCandidateFilterController {

    private final CandidateFilterService candidateFilterService;

    @PostMapping("/filter")
    @Operation(summary = "Filter candidates", description = "Filter candidates based on multiple criteria")
    public ResponseEntity<PagedResponse<CandidateFilterResponse>> filterCandidates(
            @RequestBody CandidateFilterRequest request) {
        try {
            log.info("Admin filtering candidates with criteria: {}", request);

            Page<CandidateFilterResponse> candidatesPage = candidateFilterService.filterCandidates(request);

            PagedResponse<CandidateFilterResponse> response = new PagedResponse<>(
                    candidatesPage.getContent(),
                    candidatesPage.getNumber(),
                    candidatesPage.getSize(),
                    candidatesPage.getTotalElements(),
                    candidatesPage.getTotalPages(),
                    candidatesPage.isLast()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error filtering candidates: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/filter/options")
    @Operation(summary = "Get filter options", description = "Get all available filter options for candidate filtering")
    public ResponseEntity<FilterOptionsResponse> getFilterOptions() {
        try {
            log.info("Admin requesting filter options");
            FilterOptionsResponse options = candidateFilterService.getFilterOptions();
            return ResponseEntity.ok(options);

        } catch (Exception e) {
            log.error("Error fetching filter options: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/filter/by-skills")
    @Operation(summary = "Find candidates with any of the specified skills")
    public ResponseEntity<List<CandidateFilterResponse>> findCandidatesWithAnySkill(
            @RequestBody Map<String, List<String>> request) {
        try {
            List<String> skills = request.get("skills");
            log.info("Admin searching candidates with any skills: {}", skills);

            List<CandidateFilterResponse> candidates = candidateFilterService.findCandidatesWithAnySkill(skills);
            return ResponseEntity.ok(candidates);

        } catch (Exception e) {
            log.error("Error finding candidates with skills: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    @PostMapping("/filter/by-licenses")
    @Operation(summary = "Find candidates with any of the specified licenses")
    public ResponseEntity<List<CandidateFilterResponse>> findCandidatesWithAnyLicense(
            @RequestBody Map<String, List<String>> request) {
        try {
            List<String> licenses = request.get("licenses");
            log.info("Admin searching candidates with any licenses: {}", licenses);

            List<CandidateFilterResponse> candidates = candidateFilterService.findCandidatesWithAnyLicense(licenses);
            return ResponseEntity.ok(candidates);

        } catch (Exception e) {
            log.error("Error finding candidates with licenses: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Quick search candidates", description = "Quick search by name or phone number")
    public ResponseEntity<PagedResponse<CandidateFilterResponse>> quickSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            log.info("Admin quick search for: {}", query);

            CandidateFilterRequest request = new CandidateFilterRequest();
            request.setSearchQuery(query);
            request.setPage(page);
            request.setSize(size);

            Page<CandidateFilterResponse> candidatesPage = candidateFilterService.filterCandidates(request);

            PagedResponse<CandidateFilterResponse> response = new PagedResponse<>(
                    candidatesPage.getContent(),
                    candidatesPage.getNumber(),
                    candidatesPage.getSize(),
                    candidatesPage.getTotalElements(),
                    candidatesPage.getTotalPages(),
                    candidatesPage.isLast()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error in quick search: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }
}