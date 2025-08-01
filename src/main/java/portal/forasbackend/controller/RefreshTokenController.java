package portal.forasbackend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.exception.technical.InvalidTokenException;
import portal.forasbackend.service.Admin.AdminAuthService;
import portal.forasbackend.service.Candidate.CandidateService;
import portal.forasbackend.service.Employer.EmployerService;
import portal.forasbackend.service.JwtService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtService jwtService;
    private final CandidateService candidateService;
    private final EmployerService employerService;
    private final AdminAuthService adminService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract JWT token from cookie
            String token = extractTokenFromCookie(request);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "No token found"));
            }

            // Validate current token and get claims
            JwtService.JwtClaims claims = jwtService.validateTokenAndGetClaims(token);
            String userType = claims.userType();
            Long userId = claims.userId();

            // Generate new token based on user type
            String newToken = null;
            Map<String, Object> responseData = null;

            switch (userType) {
                case "ROLE_CANDIDATE":
                    Optional<Candidate> candidate = candidateService.findById(userId);
                    if (candidate.isPresent()) {
                        newToken = jwtService.generateToken(candidate.get());
                        responseData = Map.of(
                                "id", candidate.get().getId(),
                                "name", candidate.get().getName(),
                                "type", "candidate"
                        );
                    }
                    break;

                case "ROLE_EMPLOYER":
                    Optional<Employer> employer = employerService.findById(userId);
                    if (employer.isPresent()) {
                        newToken = jwtService.generateToken(employer.get());
                        responseData = Map.of(
                                "id", employer.get().getId(),
                                "name", employer.get().getCompanyName(),
                                "type", "employer"
                        );
                    }
                    break;

                case "ROLE_SUPER_ADMIN":
                    Optional<Admin> admin = adminService.findById(userId);
                    if (admin.isPresent()) {
                        newToken = jwtService.generateToken(admin.get());
                        responseData = Map.of(
                                "id", admin.get().getId(),
                                "name", admin.get().getName(),
                                "role", admin.get().getRole()
                        );
                    }
                    break;

                default:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Unknown user type"));
            }

            if (newToken == null || responseData == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "User not found"));
            }

            // Set new token as HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", newToken)
                    .httpOnly(true)
                    .secure(true) // Enable in production with HTTPS
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            log.info("Token refreshed successfully for user: {} ({})", userId, userType);

            return ResponseEntity.ok(responseData);

        } catch (InvalidTokenException e) {
            log.warn("Invalid token during refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to refresh token"));
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}