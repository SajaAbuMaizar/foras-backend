package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.employer.JobRequest;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Job;
import portal.forasbackend.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/employer/job-application")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
