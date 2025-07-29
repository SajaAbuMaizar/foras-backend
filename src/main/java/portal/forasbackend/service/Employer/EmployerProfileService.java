package portal.forasbackend.service.Employer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.request.employer.UpdateEmployerProfileRequest;
import portal.forasbackend.dto.response.employer.EmployerProfileResponse;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.core.exceptions.business.NotFoundException;
import portal.forasbackend.mapper.EmployerProfileMapper;
import portal.forasbackend.repository.EmployerRepository;
import portal.forasbackend.service.CloudinaryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployerProfileService {

    private final EmployerRepository employerRepository;
    private final EmployerProfileMapper employerProfileMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public EmployerProfileResponse getEmployerProfile(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found"));

        return employerProfileMapper.toProfileResponse(employer);
    }

    @Transactional
    public EmployerProfileResponse updateEmployerProfile(Long employerId, UpdateEmployerProfileRequest request) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found"));

        // Check if phone number is being changed and already exists
        if (!employer.getPhone().equals(request.getPhone()) &&
                employerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        employerProfileMapper.updateEmployerFromRequest(request, employer);

        Employer savedEmployer = employerRepository.save(employer);
        log.info("Updated profile for employer: {}", employerId);

        return employerProfileMapper.toProfileResponse(savedEmployer);
    }

    @Transactional
    public String updateEmployerLogo(Long employerId, MultipartFile file) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found"));

        // Delete old logo if exists
        if (employer.getCompanyLogoUrl() != null) {
            // You might want to implement deletion from Cloudinary here
            log.info("Replacing existing logo for employer: {}", employerId);
        }

        String logoUrl = cloudinaryService.uploadEmployerLogo(file);
        employer.setCompanyLogoUrl(logoUrl);

        employerRepository.save(employer);
        log.info("Updated logo for employer: {}", employerId);

        return logoUrl;
    }

    @Transactional
    public void deleteEmployerLogo(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found"));

        if (employer.getCompanyLogoUrl() != null) {
            // You might want to implement deletion from Cloudinary here
            employer.setCompanyLogoUrl(null);
            employerRepository.save(employer);
            log.info("Deleted logo for employer: {}", employerId);
        }
    }
}