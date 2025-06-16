package portal.forasbackend.service.Employer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.dto.response.employer.EmployerLogoUrlDTO;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.repository.EmployerRepository;
import portal.forasbackend.service.CloudinaryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;
    private final CloudinaryService cloudinaryService;


    public Optional<Employer> findById(Long employerId) {
        return employerRepository.findById(employerId);
    }

    public Optional<Employer> findByPhone(String phone) {
        return employerRepository.findByPhone(phone);
    }

    public void saveLogoForEmployer(String phone, MultipartFile file) {
        Employer employer = employerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        String logoUrl = cloudinaryService.uploadEmployerLogo(file);
        employer.setCompanyLogoUrl(logoUrl);

        employerRepository.save(employer);
    }

    public List<EmployerLogoUrlDTO> getAllCompanyLogos() {
        return employerRepository.findAll().stream()
                .filter(e -> e.getCompanyLogoUrl() != null && !e.getCompanyLogoUrl().isBlank())
                .map(e -> new EmployerLogoUrlDTO(e.getId(), e.getCompanyLogoUrl()))
                .toList();
    }

}

