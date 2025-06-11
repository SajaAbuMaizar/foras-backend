package portal.forasbackend.controller;

import com.sun.tools.javac.Main;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.request.job.JobSearchRequest;
import portal.forasbackend.dto.response.job.EmployerDashboardJobListResponse;
import portal.forasbackend.dto.response.job.JobSearchResponseDTO;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.dto.response.job.EmployerJobDetailsResponse;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Job;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.data.domain.Pageable;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @PostMapping(path = "/job-application",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postJob(
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("city") Long cityId,
            @RequestParam("jobType") String jobType,
            @RequestParam("industry") Long industryId,
            @RequestParam("salary") String salary,
            @RequestParam("requiredQualifications") String requiredQualifications,
            @RequestParam("jobImage") MultipartFile jobImage,
            @RequestParam(name = "transportation", required = false, defaultValue = "false") boolean transportation,
            @RequestParam(name = "hebrew", required = false, defaultValue = "false") boolean hebrew,
      //      @RequestParam(name = "latitude", required = false) Double latitude,
          //  @RequestParam(name = "longitude", required = false) Double longitude,
            @AuthenticationPrincipal Employer employer
    ) {

        JobRequest request = new JobRequest();
        request.setJobTitle(jobTitle);
        request.setJobDescription(jobDescription);
        request.setCityId(cityId);
        request.setJobType(jobType);
        request.setIndustryId(industryId);
        request.setSalary(salary);
        request.setRequiredQualifications(requiredQualifications);
        request.setTransportation(transportation);
        request.setHebrew(hebrew);
        //request.setLatitude(latitude);
        //request.setLongitude(longitude);

        String employerPhone = employer.getPhone();
        Job createdJob = jobService.createJob(request, jobImage, employerPhone);

        return ResponseEntity.ok(createdJob);
    }

    @GetMapping
    public ResponseEntity<Page<MainPageJobListResponse>> getAllJobs(
            @PageableDefault(size = 9) Pageable pageable) {  // ✅ Fetch 9 jobs per page
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }




    @GetMapping("/my-jobs")
    public List<EmployerDashboardJobListResponse> getMyJobs(@AuthenticationPrincipal Employer employer) {
        Long employerId = employer.getId();

        return jobService.findByEmployerId(employerId);
    }

    // JobController.java
    @GetMapping("/job-details/{id}")
    public ResponseEntity<EmployerJobDetailsResponse> getJobDetailsById(@PathVariable Long id) {

        EmployerJobDetailsResponse jobDetails = jobService.getJobDetailsById(id);

        if (jobDetails == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(jobDetails);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<MainPageJobListResponse>> searchJobs(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String hebrewRequired,
            @RequestParam(required = false) String transportationAvailable,
            @PageableDefault(size = 9) Pageable pageable) {

        System.out.println("City: " + city + ", Industry: " + industry +
                ", Hebrew Required: " + hebrewRequired + ", Transportation Available: " + transportationAvailable);

        // "all" should be treated as null
        String normalizedCity = ("all".equalsIgnoreCase(city)) ? null : city;
        String normalizedIndustry = ("all".equalsIgnoreCase(industry)) ? null : industry;

        Boolean isHebrewRequired = ("true".equalsIgnoreCase(hebrewRequired)) ? Boolean.TRUE :
                ("false".equalsIgnoreCase(hebrewRequired)) ? Boolean.FALSE : null;

        Boolean isTransportAvailable = ("true".equalsIgnoreCase(transportationAvailable)) ? Boolean.TRUE :
                ("false".equalsIgnoreCase(transportationAvailable)) ? Boolean.FALSE : null;

        return ResponseEntity.ok(
                jobService.searchJobs(normalizedCity, normalizedIndustry, isHebrewRequired, isTransportAvailable, pageable)
        );
    }







}