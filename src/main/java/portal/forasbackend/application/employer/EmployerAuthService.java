package portal.forasbackend.application.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import portal.forasbackend.dto.request.employer.EmployerLoginRequest;
import portal.forasbackend.dto.request.employer.EmployerSignupRequest;
import portal.forasbackend.domain.employer.model.Employer;
import portal.forasbackend.exception.business.PhoneExistsException;
import portal.forasbackend.domain.employer.repository.EmployerRepository;
import portal.forasbackend.service.JwtService;

@Service
@RequiredArgsConstructor
public class EmployerAuthService {

    private final EmployerRepository employerRepository;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerEmployer(EmployerSignupRequest request) {
        if (employerRepository.existsByPhone(request.getPhone())) {
            throw new PhoneExistsException(request.getPhone());
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "رقم الهاتف غير مسجل. يرجى التسجيل أولاً"
                ));

        if (!passwordEncoder.matches(request.getPassword(), employer.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "كلمة المرور غير صحيحة. يرجى المحاولة مرة أخرى"
            );
        }

//        if (!employer.isActive()) {
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN,
//                    "الحساب معطل. يرجى التواصل مع الدعم"
//            );
//        }

        return jwtService.generateToken(employer);
    }
}
