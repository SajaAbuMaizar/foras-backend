package portal.forasbackend.domain.job.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.job.JobRequest;
import portal.forasbackend.dto.response.job.*;
import portal.forasbackend.domain.job.model.Job;
import portal.forasbackend.domain.job.model.JobType;
import portal.forasbackend.domain.employer.model.Employer;
import portal.forasbackend.domain.job.repository.JobRepository;
import portal.forasbackend.domain.job.repository.JobTypeRepository;
import portal.forasbackend.domain.job.repository.JobTranslationRepository;
import portal.forasbackend.domain.employer.repository.EmployerRepository;
import portal.forasbackend.domain.job.service.JobTranslationService;
import portal.forasbackend.service.CloudinaryService;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.enums.JobStatus;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.IndustryRepository;
import portal.forasbackend.repository.specification.JobSpecification;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CityRepository cityRepository;
    private final IndustryRepository industryRepository;
    private final JobTypeRepository jobTypeRepository;
    private final EmployerRepository employerRepository;
    private final JobTranslationService jobTranslationService;
    private final CloudinaryService cloudinaryService;
    private final JobMapper jobMapper;

    public Optional<Job> findById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    @Transactional
    public Job createJob(JobRequest request, MultipartFile jobImage, String employerPhone) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new EntityNotFoundException("Industry not found"));

        JobType jobType = jobTypeRepository.findById(request.getJobTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Work type not found"));

        Employer employer = employerRepository.findByPhone(employerPhone)
                .orElseThrow(() -> new EntityNotFoundException("Employer not found"));

        // Upload image to Cloudinary
        String imageUrl = cloudinaryService.uploadJobImage(jobImage);

        Job job = Job.builder()
                .salary(request.getSalary())
                .jobType(jobType)
                .imageUrl(imageUrl)
                .transportationAvailable(request.isTransportation())
                .hebrewRequired(request.isHebrew())
                .city(city)
                .industry(industry)
                .employer(employer)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(JobStatus.PENDING)
                .build();

        job = jobRepository.save(job);

        // Create original translation
        jobTranslationService.createOriginal(
                job,
                request.getLanguage(),
                request.getJobTitle(),
                request.getJobDescription(),
                request.getRequiredQualifications()
        );

        return job;
    }


    public Page<MainPageJobListResponse> getAllApprovedJobsWithArabic(Pageable pageable) {
        Page<Job> approvedJobs = jobRepository.findByStatus(JobStatus.APPROVED, pageable);

        List<MainPageJobListResponse> responses = approvedJobs.getContent().stream()
                .map(job -> jobTranslationService.getArabicTranslation(job.getId())
                        .map(t -> jobMapper.toDto(job, t))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return new PageImpl<>(responses, pageable, approvedJobs.getTotalElements());
    }


    public List<EmployerDashboardJobListResponse> findByEmployerId(Long employerId) {
        return jobRepository.findByEmployerIdOrderByCreatedAtDesc(employerId).stream()
                .map(job -> {
                    JobTranslation original = job.getTranslations().stream()
                            .filter(JobTranslation::isOriginal)
                            .findFirst().orElse(null);
                    return EmployerDashboardJobListResponse.builder()
                            .id(job.getId())
                            .jobTitle(original != null ? original.getTitle() : "")
                            .jobDescription(original != null ? original.getDescription() : "")
                            .salary(job.getSalary())
                            .cityName(job.getCity())
                            .status(job.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public EmployerJobDetailsResponse getEmployerJobDetailsById(Long jobId) {
        Job job = jobRepository.findByIdWithCandidates(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        return EmployerJobDetailsResponse.from(job);
    }

    public AdminJobDetailsResponse getAdminJobDetailsById(Long jobId) {
        Job job = jobRepository.findByIdWithDetails(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        return AdminJobDetailsResponse.from(job);
    }


    public Page<MainPageJobListResponse> searchJobs(
            String cityCode,
            String industryCode,
            Boolean hebrewRequired,
            Boolean transportationAvailable,
            Pageable pageable) {

        Page<Job> jobs = jobRepository.findAll(
                JobSpecification.withFilters(cityCode, industryCode, hebrewRequired, transportationAvailable),
                pageable
        );

        List<MainPageJobListResponse> content = jobs.getContent().stream()
                .map(job -> jobTranslationService.getArabicTranslation(job.getId())
                        .map(t -> jobMapper.toDto(job, t))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return new PageImpl<>(content, pageable, jobs.getTotalElements());
    }


    public List<AdminDashboardJobListResponse> getAllJobsForAdmin() {
        return jobRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(job -> {
                    JobTranslation originalTranslation = job.getTranslations().stream()
                            .filter(JobTranslation::isOriginal)
                            .findFirst()
                            .orElse(null);

                    return AdminDashboardJobListResponse.builder()
                            .id(job.getId())
                            .jobTitle(originalTranslation != null ? originalTranslation.getTitle() : "—")
                            .jobDescription(originalTranslation != null ? originalTranslation.getDescription() : "—")
                            .salary(job.getSalary())
                            .cityName(job.getCity())
                            .status(job.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void approveJob(Long jobId, Admin admin) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));

        boolean hasArabic = job.getTranslations().stream()
                .anyMatch(t -> "ar".equals(t.getLanguage()));

        if (!hasArabic) {
            throw new IllegalStateException("Arabic translation is required before approval.");
        }

        job.approve(admin);
    }

    @Transactional
    public void updateJobDate(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found or unauthorized"));

        if (job.getStatus() != JobStatus.APPROVED) {
            throw new IllegalStateException("Only approved jobs can be boosted");
        }

        job.updatePublishDate();
    }

}