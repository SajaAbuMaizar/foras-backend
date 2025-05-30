package portal.forasbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.entity.Employer;
import portal.forasbackend.service.CandidateService;
import portal.forasbackend.service.EmployerService;
import portal.forasbackend.service.JwtService;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final CandidateService candidateService;
    private final EmployerService employerService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(value = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            JwtService.JwtClaims claims = jwtService.validateTokenAndGetClaims(token);
            String userType = claims.userType(); // "CANDIDATE" or "EMPLOYER"
            Long userId = claims.userId();

            if ("ROLE_CANDIDATE".equals(userType)) {
                Candidate candidate = candidateService.findById(userId).orElseThrow();
                return ResponseEntity.ok().body(Map.<String, Object>of(
                        "name", candidate.getName(),
                        "type", "candidate"
                ));
            } else if ("ROLE_EMPLOYER".equals(userType)) {
                Employer employer = employerService.findById(userId).orElseThrow();
                return ResponseEntity.ok().body(Map.<String, Object>of(
                        "name", employer.getCompanyName(),
                        "type", "employer"
                ));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}