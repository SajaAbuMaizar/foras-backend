package portal.forasbackend.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.repository.AdminRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;

    public Optional<Admin> findById(Long adminId) {
        return adminRepository.findById(adminId);
    }

    public Optional<Admin> findByPhone(String phone) {
        return adminRepository.findByPhone(phone);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}