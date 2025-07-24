package portal.forasbackend.service.Admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.LocalizedNameDto;
import portal.forasbackend.dto.request.admin.AdminJobRequest;
import portal.forasbackend.dto.response.admin.JobListResponse;
import portal.forasbackend.entity.*;
import portal.forasbackend.enums.JobStatus;
import portal.forasbackend.mapper.JobMapper;
import portal.forasbackend.repository.*;
import portal.forasbackend.service.CloudinaryService;
import portal.forasbackend.service.JobTranslationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminJobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final CityRepository cityRepository;
    private final IndustryRepository industryRepository;
    private final JobTypeRepository jobTypeRepository;
    private final JobTranslationService jobTranslationService;
    private final CloudinaryService cloudinaryService;
    private final AdminRepository adminRepository;
    private final JobMapper jobMapper;

    public List<JobListResponse> getRecentJobs(int limit) {
        log.info("Fetching {} recent jobs", limit);

        return jobRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .map(job -> {
                    // Get the first translation as "original"
                    var translation = job.getTranslations() != null && !job.getTranslations().isEmpty()
                            ? job.getTranslations().get(0)
                            : null;
                    return JobListResponse.builder()
                            .id(job.getId().toString())
                            .jobTitle(translation != null ? translation.getTitle() : null)
                            .cityName(LocalizedNameDto.from(job.getCity()))
                            .jobDescription(translation != null ? translation.getDescription() : null)
                            .salary(job.getSalary())
                            .status(job.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public Job createSeedJob(AdminJobRequest request, MultipartFile jobImage, MultipartFile companyLogoFile, Long adminId) {
        log.info("Admin creating seed job with company: {}", request.getCompanyName());

        // Upload company logo if provided
        String companyLogoUrl = request.getCompanyLogoUrl();
        if (companyLogoFile != null && !companyLogoFile.isEmpty()) {
            companyLogoUrl = cloudinaryService.uploadEmployerLogo(companyLogoFile);
        }

        // Create or find employer
        Employer employer = findOrCreateSeedEmployer(request, companyLogoUrl);

        // Create a job
        Job job = createJobFromAdminRequest(request, employer, jobImage);

        job = jobRepository.save(job);

        // Create translation
        createJobTranslation(job, request);

        // Auto-approve if requested
        if (request.isAutoApprove()) {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            job.approve(admin);
        }

        return job;
    }

    private Employer findOrCreateSeedEmployer(AdminJobRequest request, String companyLogoUrl) {
        // Check if employer with this name already exists
        return employerRepository.findByCompanyName(request.getCompanyName())
                .orElseGet(() -> createSeedEmployer(request, companyLogoUrl));
    }

    private Employer createSeedEmployer(AdminJobRequest request, String companyLogoUrl) {
        // Create password encoder (you might want to make this a bean instead)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String seedPhone = request.getCompanyPhone() != null ?
                request.getCompanyPhone() :
                generateSeedPhone();

        String seedEmail = request.getCompanyEmail() != null ?
                request.getCompanyEmail() :
                generateSeedEmail(request.getCompanyName());

        Employer employer = Employer.builder()
                .companyName(request.getCompanyName())
                .phone(seedPhone)
                .email(seedEmail)
                .companyLogoUrl(companyLogoUrl)
                .name("Admin Seed")
                .password(passwordEncoder.encode("1q2w3e4r")) // Properly hashed password
                .role("ROLE_EMPLOYER")
                .isSeedData(true)
                .lastLogin(LocalDateTime.now())
                .build();

        return employerRepository.save(employer);
    }

    private Job createJobFromAdminRequest(AdminJobRequest request, Employer employer, MultipartFile jobImage) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        JobType jobType = jobTypeRepository.findById(request.getJobTypeId())
                .orElseThrow(() -> new RuntimeException("Job type not found"));

        String imageUrl = null;
        if (jobImage != null && !jobImage.isEmpty()) {
            imageUrl = cloudinaryService.uploadJobImage(jobImage);
        }

        return Job.builder()
                .employer(employer)
                .city(city)
                .industry(industry)
                .salary(request.getSalary())
                .jobType(jobType)
                .imageUrl(imageUrl)
                .transportationAvailable(request.isTransportation())
                .hebrewRequired(request.isHebrew())
                .status(JobStatus.PENDING)
                .build();
    }

    private void createJobTranslation(Job job, AdminJobRequest request) {
        JobTranslation translation = JobTranslation.builder()
                .job(job)
                .language(request.getLanguage())
                .title(request.getJobTitle())
                .description(request.getJobDescription())
                .requiredQualifications(request.getRequiredQualifications())
                .isOriginal(true)
                .build();

        jobTranslationService.save(translation);
    }

    private String generateSeedPhone() {
        // Generate unique phone for seed data
        return "050" + System.currentTimeMillis() % 10000000;
    }

    private String generateSeedEmail(String companyName) {
        String cleanName = companyName.toLowerCase()
                .replaceAll("[^a-z0-9]", "");
        return cleanName + "@seed.example.com";
    }
}