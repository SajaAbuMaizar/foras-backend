package portal.forasbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import portal.forasbackend.dto.request.candidate.AuthRequest;
import portal.forasbackend.entity.Candidate;
import portal.forasbackend.exception.technical.AuthException;
import portal.forasbackend.service.JwtService;
import portal.forasbackend.service.CandidateService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/candidate")
public class CandidateAuthController {
    @Autowired private CandidateService candidateService;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

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

            return ResponseEntity.ok().body(Map.of("name", candidate.getName()));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "حدث خطأ أثناء تسجيل الدخول"));
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
