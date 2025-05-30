package portal.forasbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import portal.forasbackend.dto.request.employer.EmployerLoginRequest;
import portal.forasbackend.dto.request.employer.EmployerSignupRequest;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.exception.business.PhoneExistsException;
import portal.forasbackend.repository.EmployerRepository;

@Service
@RequiredArgsConstructor
public class EmployerAuthService {

    private final EmployerRepository employerRepository;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerEmployer(EmployerSignupRequest request) {
        if (employerRepository.existsByPhone(request.getPhone())) {
            throw new PhoneExistsException("رقم الهاتف مستخدم بالفعل");
        }

        Employer employer = new Employer();
        employer.setName(request.getName());
        employer.setCompanyName(request.getCompanyName());
        employer.setEmail(request.getEmail());
        employer.setPhone(request.getPhone());
        employer.setPassword(passwordEncoder.encode(request.getPassword()));

        employerRepository.save(employer);

        return jwtService.generateToken(employer);
    }


    public String loginEmployer(EmployerLoginRequest request) {
        Employer employer = employerRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("رقم الهاتف غير مسجل"));

        if (!passwordEncoder.matches(request.getPassword(), employer.getPassword())) {
            throw new RuntimeException("كلمة المرور غير صحيحة");
        }

        return jwtService.generateToken(employer);

    }
}
