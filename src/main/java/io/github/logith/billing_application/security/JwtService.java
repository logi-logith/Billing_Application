package io.github.logith.billing_application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Injected from application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // ─── Generate Token ─────────────────────────────────────
    public String generateToken(Long userId, String role) {
        return Jwts.builder()
                .subject(String.valueOf(userId))       // who this token belongs to
                .claim("role", role)                   // custom claim
                .issuedAt(new Date())                  // issued now
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())             // sign with secret
                .compact();                            // build the string
    }

    // ─── Extract userId from token ──────────────────────────
    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    // ─── Extract role from token ────────────────────────────
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // ─── Validate token ─────────────────────────────────────
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // throws if invalid/expired
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Private helpers ────────────────────────────────────
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