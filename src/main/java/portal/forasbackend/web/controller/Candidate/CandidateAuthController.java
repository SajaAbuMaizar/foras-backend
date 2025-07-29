package portal.forasbackend.web.controller.Candidate;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.application.dto.request.candidate.AuthRequest;
import portal.forasbackend.application.dto.request.candidate.CandidateSignupRequestDTO;
import portal.forasbackend.application.dto.response.candidate.CandidateSignupResponseDTO;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.core.exception.technical.AuthException;
import portal.forasbackend.application.service.Candidate.CandidateAuthService;
import portal.forasbackend.application.service.JwtService;
import portal.forasbackend.application.service.Candidate.CandidateService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/candidate")
@RequiredArgsConstructor
public class CandidateAuthController {

     private final CandidateService candidateService;
     private final JwtService jwtService;
     private final PasswordEncoder passwordEncoder;
     private final CandidateAuthService candidateAuthService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        try {
            Candidate candidate = candidateService.findByPhone(request.getPhone())
                    .orElseThrow(() -> new AuthException("رقم الهاتف غير مسجل"));

            if (!passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
                throw new AuthException("كلمة المرور غير صحيحة");
            }

            String token = jwtService.generateToken(candidate);
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok().body(Map.of(
                    "id", candidate.getId(),
                    "name", candidate.getName(),
                    "type", "candidate"
            ));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "حدث خطأ أثناء تسجيل الدخول"));
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerCandidate(@RequestBody CandidateSignupRequestDTO request, HttpServletResponse response) {
        String jwt = candidateAuthService.registerCandidate(request);

        // Set JWT as HTTP-only cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("status", "success"));
    }


}
