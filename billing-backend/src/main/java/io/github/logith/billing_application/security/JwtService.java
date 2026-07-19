package io.github.logith.billing_application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // ─── Generate Token ──────────────────────────────────────────────────────
    public String generateToken(Long userId, String role) {
        log.debug("Generating JWT for userId: {}, role: {}", userId, role);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // ─── Extract userId ──────────────────────────────────────────────────────
    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    // ─── Extract role ────────────────────────────────────────────────────────
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // ─── Validate token ──────────────────────────────────────────────────────
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during JWT validation", e);
            return false;
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
