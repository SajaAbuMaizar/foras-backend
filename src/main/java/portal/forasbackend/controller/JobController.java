package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.response.job.*;
import portal.forasbackend.dto.response.shared.PagedResponse;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.domain.employer.model.Employer;
import portal.forasbackend.domain.job.model.Job;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.domain.job.service.JobService;
import portal.forasbackend.domain.job.service.JobTranslationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;
    private final JobTranslationService jobTranslationService;

    @PostMapping(path = "/job-application", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postJob(
            @RequestParam("language") String language,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("city") Long cityId,
            @RequestParam("jobType") Long jobTypeId,
            @RequestParam("industry") Long industryId,
            @RequestParam("salary") String salary,
            @RequestParam("requiredQualifications") String requiredQualifications,
            @RequestParam("jobImage") MultipartFile jobImage,
            @RequestParam(name = "transportation", required = false, defaultValue = "false") boolean transportation,
            @RequestParam(name = "hebrew", required = false, defaultValue = "false") boolean hebrew,
            @AuthenticationPrincipal Employer employer
    ) {

        // Validate language input
        if (!"he".equals(language) && !"ar".equals(language)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Invalid language code. Must be 'he' or 'ar'"
            ));
        }
        JobRequest request = new JobRequest();
        request.setJobTitle(jobTitle);
        request.setJobDescription(jobDescription);
        request.setLanguage(language);
        request.setCityId(cityId);
        request.setJobTypeId(jobTypeId);
        request.setIndustryId(industryId);
        request.setSalary(salary);
        request.setRequiredQualifications(requiredQualifications);
        request.setTransportation(transportation);
        request.setHebrew(hebrew);
        // latitude, longitude if used


        String employerPhone = employer.getPhone();

        Job createdJob = jobService.createJob(request, jobImage, employerPhone);

        return ResponseEntity.ok(createdJob);
    }


    @GetMapping
    public ResponseEntity<PagedResponse<MainPageJobListResponse>> getAllJobs(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MainPageJobListResponse> page = jobService.getAllApprovedJobsWithArabic(pageable);
        PagedResponse<MainPageJobListResponse> response = new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/admin/jobs")
    public ResponseEntity<List<AdminDashboardJobListResponse>> getAllJobsForAdmin() {
        return ResponseEntity.ok(jobService.getAllJobsForAdmin());
    }


    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/my-jobs")
    public List<EmployerDashboardJobListResponse> getMyJobs(@AuthenticationPrincipal Employer employer) {
        Long employerId = employer.getId();

        return jobService.findByEmployerId(employerId);
    }


    @GetMapping("/employer/job-details/{id}")
    public ResponseEntity<EmployerJobDetailsResponse> getJobDetailsById(@PathVariable Long id) {

        EmployerJobDetailsResponse jobDetails = jobService.getEmployerJobDetailsById(id);

        if (jobDetails == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(jobDetails);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/admin/job-details/{id}")
    public ResponseEntity<AdminJobDetailsResponse> getJobDetails(@PathVariable Long id) {
        AdminJobDetailsResponse jobDetails = jobService.getAdminJobDetailsById(id);
        if (jobDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobDetails);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping("/admin/translate-job/{id}")
    public ResponseEntity<Void> translateJob(
            @PathVariable Long id,
            @RequestParam("translatedTitle") String translatedTitle,
            @RequestParam("translatedDescription") String translatedDescription,
            @RequestParam("translatedRequiredQualifications") String translatedQualifications
    ) {
        jobTranslationService.upsertArabicTranslation(id, translatedTitle, translatedDescription, translatedQualifications);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping("/admin/approve-job/{id}")
    public ResponseEntity<Void> approveJob(@PathVariable Long id, @AuthenticationPrincipal Admin admin) {
        jobService.approveJob(id, admin);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public ResponseEntity<PagedResponse<MainPageJobListResponse>> searchJobs(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String hebrewRequired,
            @RequestParam(required = false) String transportationAvailable,
            @PageableDefault(size = 9) Pageable pageable) {

        String normalizedCity = ("all".equalsIgnoreCase(city)) ? null : city;
        String normalizedIndustry = ("all".equalsIgnoreCase(industry)) ? null : industry;

        Boolean isHebrewRequired = ("true".equalsIgnoreCase(hebrewRequired)) ? Boolean.TRUE :
                ("false".equalsIgnoreCase(hebrewRequired)) ? Boolean.FALSE : null;

        Boolean isTransportAvailable = ("true".equalsIgnoreCase(transportationAvailable)) ? Boolean.TRUE :
                ("false".equalsIgnoreCase(transportationAvailable)) ? Boolean.FALSE : null;

        Page<MainPageJobListResponse> page = jobService.searchJobs(
                normalizedCity, normalizedIndustry, isHebrewRequired, isTransportAvailable, pageable
        );

        PagedResponse<MainPageJobListResponse> response = new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/update-date")
    public ResponseEntity<Void> updateJobDate(@PathVariable Long id) {
        jobService.updateJobDate(id);
        return ResponseEntity.ok().build();
    }


}