package portal.forasbackend.controller.Employer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.dto.request.employer.EmployerSignupRequest;
import portal.forasbackend.dto.request.employer.EmployerLoginRequest;
import portal.forasbackend.service.Employer.EmployerAuthService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/employer")
@RequiredArgsConstructor
public class EmployerAuthController {

    private final EmployerAuthService employerAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody EmployerSignupRequest request, HttpServletResponse response) {
        String jwt = employerAuthService.registerEmployer(request);

        // Set JWT as HTTP-only cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "تم تسجيل الحساب بنجاح"
        ));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Valid EmployerLoginRequest request, HttpServletResponse response) {
        String jwt = employerAuthService.loginEmployer(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "تم تسجيل الدخول بنجاح"));
    }


}
