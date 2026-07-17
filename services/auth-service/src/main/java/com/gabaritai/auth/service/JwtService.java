package com.gabaritai.auth.service;

import com.gabaritai.auth.config.JwtProperties;
import com.gabaritai.auth.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private static final int MIN_SECRET_BYTES = 32;

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        byte[] secretBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "app.jwt.secret (JWT_SECRET) deve ter pelo menos 32 bytes para assinatura HS256.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getExpirationSeconds(), ChronoUnit.SECONDS);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public long getExpirationSeconds() {
        return properties.getExpirationSeconds();
    }
}
