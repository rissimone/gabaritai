package com.gabaritai.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabaritai.auth.config.JwtProperties;
import com.gabaritai.auth.domain.User;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtProperties propertiesWithSecret(String secret) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(secret);
        properties.setExpirationSeconds(3600);
        return properties;
    }

    @Test
    void generateAccessToken_containsSubjectAndEmailClaims() {
        JwtService jwtService = new JwtService(propertiesWithSecret("test-only-secret-key-must-be-at-least-32-bytes"));
        User user = new User(UUID.randomUUID(), "Ana", "ana@example.com", "hashed-password", Instant.now());

        String token = jwtService.generateAccessToken(user);

        var claims = Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                        "test-only-secret-key-must-be-at-least-32-bytes".getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(user.getId().toString());
        assertThat(claims.get("email", String.class)).isEqualTo("ana@example.com");
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }

    @Test
    void constructor_withTooShortSecret_throwsIllegalStateException() {
        assertThatThrownBy(() -> new JwtService(propertiesWithSecret("too-short")))
                .isInstanceOf(IllegalStateException.class);
    }
}
