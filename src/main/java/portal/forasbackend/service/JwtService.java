package portal.forasbackend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import portal.forasbackend.common.model.User;
import portal.forasbackend.core.exceptions.technical.InvalidTokenException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:604800000}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration:2592000000}")
    private long refreshExpiration;

    private SecretKey secretKey;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() {
        try {
            // Create a 256-bit key from the secret
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = sha.digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            this.secretKey = Keys.hmacShaKeyFor(key);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JWT secret key", e);
        }
    }

    public TokenPair generateTokenPair(User user) {
        Date now = new Date();
        Date accessExpiry = Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS));
        Date refreshExpiry = Date.from(Instant.now().plus(refreshExpiration, ChronoUnit.MILLIS));

        String jti = UUID.randomUUID().toString();

        String accessToken = Jwts.builder()
                .setId(jti)
                .setSubject(user.getId().toString())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(accessExpiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getId().toString())
                .claim("type", "refresh")
                .claim("accessJti", jti)
                .setIssuedAt(now)
                .setExpiration(refreshExpiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new TokenPair(accessToken, refreshToken);
    }

    public String generateToken(User user) {
        return generateTokenPair(user).accessToken();
    }

    public JwtClaims validateTokenAndGetClaims(String token) throws JwtException {
        if (blacklistedTokens.contains(token)) {
            throw new InvalidTokenException("Token has been revoked");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Validate token type
            String tokenType = claims.get("type", String.class);
            if (!"access".equals(tokenType)) {
                throw new InvalidTokenException("Invalid token type");
            }

            return new JwtClaims(
                    Long.parseLong(claims.getSubject()),
                    claims.get("role", String.class),
                    claims.getId()
            );
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token has expired");
        } catch (JwtException e) {
            log.error("JWT validation error", e);
            throw new InvalidTokenException("Invalid token");
        }
    }

    // Clean up expired tokens from blacklist periodically
    @Scheduled(fixedDelay = 3600000) // Every hour
    public void cleanupBlacklist() {
        // Implementation depends on your token storage strategy
        log.info("Cleaning up blacklisted tokens");
    }

    public record JwtClaims(Long userId, String userType, String jti) {}
    public record TokenPair(String accessToken, String refreshToken) {}
}