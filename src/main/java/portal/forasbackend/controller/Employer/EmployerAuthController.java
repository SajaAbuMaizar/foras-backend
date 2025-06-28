package portal.forasbackend.controller.Employer;

import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.employer.EmployerSignupRequest;
import portal.forasbackend.dto.request.employer.EmployerLoginRequest;
import portal.forasbackend.service.Employer.EmployerAuthService;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth/employer")
@RequiredArgsConstructor
public class EmployerAuthController {

    private final EmployerAuthService employerAuthService;

    @PostMapping("/signup")
    @Timed(value = "employer.signup", description = "Time taken to register employer")
    public ResponseEntity<?> signup(@Valid @RequestBody EmployerSignupRequest request,
                                    HttpServletResponse response) {
        log.info("New employer signup attempt for company: {}", request.getCompanyName());

        try {
            String jwt = employerAuthService.registerEmployer(request);

            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            log.info("Employer registered successfully: {}", request.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "تم تسجيل الحساب بنجاح"
            ));
        } catch (Exception e) {
            log.error("Employer signup failed", e);
            throw e;
        }
    }

    @PostMapping("/signin")
    @Timed(value = "employer.signin", description = "Time taken to login employer")
    public ResponseEntity<?> login(@Valid @RequestBody EmployerLoginRequest request,
                                   HttpServletResponse response) {
        log.debug("Employer login attempt: {}", request.getPhone());

        String jwt = employerAuthService.loginEmployer(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "تم تسجيل الدخول بنجاح"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "تم تسجيل الخروج بنجاح"));
    }
}