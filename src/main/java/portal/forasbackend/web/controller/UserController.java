package portal.forasbackend.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.application.dto.response.employer.EmployerDTO;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.application.service.Admin.AdminAuthService;
import portal.forasbackend.application.service.Candidate.CandidateService;
import portal.forasbackend.application.service.Employer.EmployerService;
import portal.forasbackend.application.service.JwtService;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final CandidateService candidateService;
    private final EmployerService employerService;
    private final AdminAuthService adminService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(value = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            JwtService.JwtClaims claims = jwtService.validateTokenAndGetClaims(token);
            String userType = claims.userType();
            Long userId = claims.userId();

            if ("ROLE_CANDIDATE".equals(userType)) {
                Candidate candidate = candidateService.findById(userId).orElseThrow();
                return ResponseEntity.ok().body(Map.of(
                        "id", candidate.getId(),
                        "name", candidate.getName(),
                        "type", "candidate"
                ));
            } else if ("ROLE_EMPLOYER".equals(userType)) {
                Employer employer = employerService.findById(userId).orElseThrow();
                EmployerDTO employerDTO = new EmployerDTO(
                        employer.getId(),
                        employer.getCompanyName(),
                        employer.getCompanyLogoUrl(),
                        "employer"
                );
                return ResponseEntity.ok().body(employerDTO);
            } else if ("ROLE_SUPER_ADMIN".equals(userType)) {
                Admin admin = adminService.findById(userId).orElseThrow();
                return ResponseEntity.ok().body(Map.of(
                        "id", admin.getId(),
                        "name", admin.getName(),
                        "type", "super_admin"
                )); } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

}