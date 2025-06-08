package portal.forasbackend.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.response.job.EmployerDashboardJobListResponse;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.dto.response.job.EmployerJobDetailsResponse;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Job;
import portal.forasbackend.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;

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

    @GetMapping("/all")
    public ResponseEntity<List<MainPageJobListResponse>> getAllJobs() {
        List<MainPageJobListResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
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



}
