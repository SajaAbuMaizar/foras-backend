package portal.forasbackend.service.Employer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.response.employer.EmployerLogoUrlDTO;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.domain.repository.EmployerRepository;
import portal.forasbackend.service.CloudinaryService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployerService {

    private final CloudinaryService cloudinaryService;
    @Autowired
    private EmployerRepository employerRepository;

    public Optional<Employer> findById(Long employerId) {
        return employerRepository.findById(employerId);
    }

    public Optional<Employer> findByPhone(String phone) {
        return employerRepository.findByPhone(phone);
    }

    @Transactional
    public String saveLogoForEmployer(String phone, MultipartFile file) {
        Employer employer = employerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        String logoUrl = cloudinaryService.uploadEmployerLogo(file);
        employer.setCompanyLogoUrl(logoUrl);
        employerRepository.save(employer);

        log.info("Logo updated for employer: {}", employer.getCompanyName());
        return logoUrl;
    }

    public List<EmployerLogoUrlDTO> getAllCompanyLogos() {
        return employerRepository.findAll().stream()
                .filter(e -> e.getCompanyLogoUrl() != null && !e.getCompanyLogoUrl().isBlank())
                .map(e -> new EmployerLogoUrlDTO(e.getId(), e.getCompanyLogoUrl()))
                .toList();
    }

    public void save(Employer employer) {
        employerRepository.save(employer);
    }

}

