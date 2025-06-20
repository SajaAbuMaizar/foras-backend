package portal.forasbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.request.job.JobSearchRequest;
import portal.forasbackend.dto.response.job.*;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.entity.Job;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.EmployerRepository;
import portal.forasbackend.repository.IndustryRepository;
import portal.forasbackend.repository.JobRepository;
import portal.forasbackend.repository.specification.JobSpecification;

import org.springframework.data.domain.Pageable;
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
    private final JobMapper jobMapper;

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

    public Page<MainPageJobListResponse> getAllJobs(Pageable pageable) {
        Page<Job> jobsPage = jobRepository.findAll(pageable);
        return jobsPage.map(jobMapper::toDto);
    }




    public List<EmployerDashboardJobListResponse> findByEmployerId(Long employerId) {
        return jobRepository.findByEmployerId(employerId).stream()
                .map(job -> EmployerDashboardJobListResponse.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .salary(job.getSalary())
                        .cityName(job.getCity())
                        .status(job.getStatus())
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


    public Page<MainPageJobListResponse> searchJobs(
            String cityCode,
            String industryCode,
            Boolean hebrewRequired,
            Boolean transportationAvailable,
            Pageable pageable) {

        Page<Job> jobPage = jobRepository.findAll(
                JobSpecification.withFilters(
                        cityCode,
                        industryCode,
                        hebrewRequired,
                        transportationAvailable
                ),
                pageable
        );

        return jobPage.map(jobMapper::toDto);
    }


    public List<AdminDashboardJobListResponse> getAllJobsForAdmin() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
                .map(job -> AdminDashboardJobListResponse.builder()
                        .id(job.getId())
                        .jobTitle(job.getJobTitle())
                        .jobDescription(job.getJobDescription())
                        .salary(job.getSalary())
                        .cityName(job.getCity())
                        .status(job.getStatus())
                        .build())
                .toList();
    }


}
