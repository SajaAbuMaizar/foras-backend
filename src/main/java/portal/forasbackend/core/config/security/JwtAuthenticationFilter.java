package portal.forasbackend.core.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import portal.forasbackend.domain.model.Admin;
import portal.forasbackend.domain.model.Candidate;
import portal.forasbackend.domain.model.Employer;
import portal.forasbackend.application.service.Admin.AdminAuthService;
import portal.forasbackend.application.service.Candidate.CandidateService;
import portal.forasbackend.application.service.Employer.EmployerService;
import portal.forasbackend.application.service.JwtService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final EmployerService employerService;
    private final AdminAuthService adminService;
    private final CandidateService candidateService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;

        // Extract token from cookie
        if (request.getCookies() != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst();
            if (jwtCookie.isPresent()) {
                token = jwtCookie.get().getValue();
            }
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                JwtService.JwtClaims claims = jwtService.validateTokenAndGetClaims(token);

                if ("ROLE_EMPLOYER".equals(claims.userType())) {
                    Employer employer = employerService.findById(claims.userId()).orElse(null);
                    if (employer != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(employer, null, employer.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } else if ("ROLE_CANDIDATE".equals(claims.userType())) {
                    Candidate candidate = candidateService.findById(claims.userId()).orElse(null);
                    if (candidate != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(candidate, null, candidate.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } else if ("ROLE_SUPER_ADMIN".equals(claims.userType())) {
                    Admin admin = adminService.findById(claims.userId()).orElse(null);
                    if (admin != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                logger.error("JWT validation error", e); // Log the error
            }
        }

        filterChain.doFilter(request, response);
    }
}