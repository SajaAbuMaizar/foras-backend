package portal.forasbackend.application.service.Admin;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.repository.AdminRepository;

@Slf4j
@Service
public class AdminSeedService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PHONE:}")
    private String adminPhone;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_NAME:}")
    private String adminName;


    public AdminSeedService(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedAdmin() {
        validateConfig();

        if (adminRepo.findByPhone(adminPhone).isEmpty()) {
            Admin admin = Admin.builder()
                    .phone(adminPhone)
                    .name(adminName)
                    .password(passwordEncoder.encode(adminPassword))
                    .role("ROLE_SUPER_ADMIN")
                    .build();

            adminRepo.save(admin);
            log.info("✅ Super Admin seeded successfully with phone: {}", adminPhone);
        }
    }

    private void validateConfig() {
        if (adminPhone.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException("""
                Admin credentials not properly configured!
                Please set ADMIN_PHONE and ADMIN_PASSWORD environment variables.
                """);
        }

        if (adminPassword.length() < 10) {
            throw new IllegalStateException("Admin password must be at least 12 characters long");
        }
    }
}