package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.employer.JobRequest;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.entity.Job;
import portal.forasbackend.repository.CityRepository;
import portal.forasbackend.repository.EmployerRepository;
import portal.forasbackend.repository.IndustryRepository;
import portal.forasbackend.repository.JobRepository;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CityRepository cityRepository;
    private final IndustryRepository industryRepository;
    private final EmployerRepository employerRepository;
    private final CloudinaryService cloudinaryService;

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
}
