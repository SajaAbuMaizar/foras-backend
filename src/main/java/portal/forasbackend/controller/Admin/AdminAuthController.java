package portal.forasbackend.controller.Admin;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portal.forasbackend.dto.request.admin.AdminAuthRequest;
import portal.forasbackend.entity.Admin;
import portal.forasbackend.exception.technical.AuthException;
import portal.forasbackend.service.Admin.AdminAuthService;
import portal.forasbackend.service.JwtService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminAuthRequest request,
                                   HttpServletResponse response) {
        try {
            Admin admin = adminService.findByPhone(request.getPhone())
                    .orElseThrow(() -> new AuthException("Phone number not registered"));

            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                throw new AuthException("Invalid password");
            }

         /*   if (!admin.isActive()) {
                throw new AuthException("Account is disabled");
            }*/

            String token = jwtService.generateToken(admin);

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true) // Enable in production with HTTPS
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(1)) // 1-day expiration
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok().body(Map.of(
                    "id", admin.getId(),
                    "name", admin.getName(),
                    "role", admin.getRole()
            ));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
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