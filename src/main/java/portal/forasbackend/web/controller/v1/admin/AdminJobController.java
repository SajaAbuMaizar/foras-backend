package portal.forasbackend.web.controller.v1.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.admin.AdminJobRequest;
import portal.forasbackend.dto.response.admin.JobListResponse;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.entity.Job;
import portal.forasbackend.core.exceptions.FileUploadException;
import portal.forasbackend.service.Admin.AdminJobService;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/seed-job")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createSeedJob(
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("language") String language,
            @RequestParam("cityId") Long cityId,
            @RequestParam("jobType") Long jobType,
            @RequestParam("industryId") Long industryId,
            @RequestParam("salary") String salary,
            @RequestParam("requiredQualifications") String requiredQualifications,
            @RequestParam("companyName") String companyName,
            @RequestParam(value = "companyDescription", required = false) String companyDescription,
            @RequestParam(value = "companyPhone") String companyPhone,
            @RequestParam(value = "companyEmail") String companyEmail,
            @RequestParam(value = "companyLogoFile", required = false) MultipartFile companyLogoFile,
            @RequestParam(value = "transportation", defaultValue = "false") boolean transportation,
            @RequestParam(value = "hebrew", defaultValue = "false") boolean hebrew,
            @RequestParam(value = "autoApprove", defaultValue = "true") boolean autoApprove,
            @RequestParam(value = "jobImage", required = false) MultipartFile jobImage,
            @AuthenticationPrincipal Admin admin
    ) {
        try {
            log.info("Admin {} creating seed job for company: {}", admin.getName(), companyName);

            AdminJobRequest request = AdminJobRequest.builder()
                    .jobTitle(jobTitle)
                    .jobDescription(jobDescription)
                    .language(language)
                    .cityId(cityId)
                    .jobTypeId(jobType)
                    .industryId(industryId)
                    .salary(salary)
                    .requiredQualifications(requiredQualifications)
                    .companyName(companyName)
                    .companyDescription(companyDescription)
                    .companyPhone(companyPhone)
                    .companyEmail(companyEmail)
                    .transportation(transportation)
                    .hebrew(hebrew)
                    .autoApprove(autoApprove)
                    .build();

            Job createdJob = adminJobService.createSeedJob(request, jobImage, companyLogoFile, admin.getId());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "jobId", createdJob.getId(),
                    "message", "تم إنشاء الوظيفة بنجاح"
            ));
        } catch (FileUploadException e) {
            log.error("File upload error creating seed job: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "errorCode", e.getErrorCode()
            ));
        } catch (Exception e) {
            log.error("Error creating seed job: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "فشل في إنشاء الوظيفة: " + e.getMessage()
            ));
        }
    }
}
