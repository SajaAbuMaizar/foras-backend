package portal.forasbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.response.job.EmployerJobDetailsResponse;
import portal.forasbackend.dto.response.job.MainPageJobListResponse;
import portal.forasbackend.dto.response.job.EmployerDashboardJobListResponse;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.entity.Job;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.EmployerRepository;
import portal.forasbackend.repository.IndustryRepository;
import portal.forasbackend.repository.JobRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CityRepository cityRepository;
    private final IndustryRepository industryRepository;
    private final EmployerRepository employerRepository;
    private final CloudinaryService cloudinaryService;

    public Optional<Job> findById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public Job createJob(JobRequest request, MultipartFile jobImage, String employerPhone) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        Employer employer = employerRepository.findByPhone(employerPhone)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        String imageUrl = cloudinaryService.uploadJobImage(jobImage);

        Job job = Job.builder()
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .salary(request.getSalary())
                .requiredQualifications(request.getRequiredQualifications())
                .jobType(request.getJobType())
                .imageUrl(imageUrl)
                .transportationAvailable(request.isTransportation())
                .hebrewRequired(request.isHebrew())
               // .latitude(request.getLatitude())
               // .longitude(request.getLongitude())
                .city(city)
                .industry(industry)
                .employer(employer)
                .build();

        return jobRepository.save(job);
    }

    // get all the jobs for the main page (in Arabic)
    public List<MainPageJobListResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(job -> MainPageJobListResponse.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .salary(job.getSalary())
                        .requiredQualifications(job.getRequiredQualifications())
                        .jobType(job.getJobType())
                        .imageUrl(job.getImageUrl())
                        .transportationAvailable(job.isTransportationAvailable())
                        .hebrewRequired(job.isHebrewRequired())
                        .cityName(job.getCity() != null ? job.getCity().getNameAr() : null)
                        .industryName(job.getIndustry() != null ? job.getIndustry().getNameAr() : null)
                        .latitude(job.getLatitude())
                        .longitude(job.getLongitude())
                        .employerId(job.getEmployer().getId())
                        .employerCompanyName(job.getEmployer().getCompanyName())
                        .employerCompanyLogoUrl(job.getEmployer().getCompanyLogoUrl())
                        .postedDate(job.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        )
                        .build())
                .collect(Collectors.toList());
    }


    public List<EmployerDashboardJobListResponse> findByEmployerId(Long employerId) {
        return jobRepository.findByEmployerId(employerId).stream()
                .map(job -> EmployerDashboardJobListResponse.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .salary(job.getSalary())
                        .cityName(job.getCity())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // JobService.java
    public EmployerJobDetailsResponse getJobDetailsById(Long jobId) {
        Job job = jobRepository.findByIdWithCandidates(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        return EmployerJobDetailsResponse.from(job);
    }


}
