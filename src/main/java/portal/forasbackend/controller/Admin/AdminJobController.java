package portal.forasbackend.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.response.admin.JobListResponse;
import portal.forasbackend.service.Admin.AdminJobService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@Tag(name = "Admin Jobs", description = "Admin job management endpoints")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminJobController {

    private final AdminJobService adminJobService;

    @GetMapping("/recent")
    @Operation(summary = "Get recent jobs", description = "Get most recent jobs for dashboard")
    public ResponseEntity<List<JobListResponse>> getRecentJobs(
            @RequestParam(defaultValue = "5") int limit) {
        try {
            log.info("Admin requesting recent {} jobs", limit);
            List<JobListResponse> jobs = adminJobService.getRecentJobs(limit);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Error fetching recent jobs: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }
}
