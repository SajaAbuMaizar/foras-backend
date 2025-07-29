package portal.forasbackend.web.controller.v1.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.response.admin.DashboardStatsResponse;
import portal.forasbackend.dto.response.admin.ActivityResponse;
import portal.forasbackend.service.Admin.AdminDashboardService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Admin dashboard endpoints")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Get comprehensive dashboard statistics")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        try {
            log.info("Admin requesting dashboard statistics");
            DashboardStatsResponse stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching dashboard stats: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/activities")
    @Operation(summary = "Get recent activities", description = "Get recent system activities")
    public ResponseEntity<List<ActivityResponse>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            log.info("Admin requesting recent activities with limit: {}", limit);
            List<ActivityResponse> activities = dashboardService.getRecentActivities(limit);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            log.error("Error fetching activities: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(List.of());
        }
    }
}