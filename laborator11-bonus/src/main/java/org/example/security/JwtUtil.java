package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final byte[] SECRET_KEY;

    public JwtUtil() {
        String secret = System.getenv("JWT_SECRET_KEY");
        if (secret == null || secret.isEmpty()) {
            log.warn("Environment variable JWT_SECRET_KEY is not set. Using default hardcoded key for development.");
            secret = "default_hardcoded_secret_key_for_dev"; // Replace with a secure key in production
        }
        this.SECRET_KEY = secret.getBytes(); // Use raw bytes for consistency
        log.info("SECRET_KEY initialized successfully: {}", new String(this.SECRET_KEY));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            log.info("Validating token for username: {}", username);
            String extractedUsername = extractUsername(token);
            boolean isExpired = isTokenExpired(token);
            log.info("Extracted username: {}. Token expired: {}", extractedUsername, isExpired);
            if (extractedUsername.equals(username) && !isExpired) {
                log.info("Token is valid for username: {}", username);
                return true;
            } else {
                log.warn("Token validation failed. Extracted username: {}. Expired: {}", extractedUsername, isExpired);
                return false;
            }
        } catch (Exception e) {
            log.error("Error during token validation", e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        try {
            log.info("Extracting claims from token");
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from token", e);
            throw e;
        }
    }
}
