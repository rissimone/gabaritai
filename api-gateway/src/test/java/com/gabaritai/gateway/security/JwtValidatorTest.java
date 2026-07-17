package com.gabaritai.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabaritai.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class JwtValidatorTest {

    private static final String SECRET = "test-only-secret-key-must-be-at-least-32-bytes-long";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private JwtValidator validatorWithSecret(String secret) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(secret);
        return new JwtValidator(properties);
    }

    private String tokenExpiringIn(long seconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject("11111111-1111-1111-1111-111111111111")
                .claim("email", "ana@example.com")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(seconds, ChronoUnit.SECONDS)))
                .signWith(KEY)
                .compact();
    }

    @Test
    void validate_withValidToken_returnsClaims() {
        JwtValidator validator = validatorWithSecret(SECRET);

        Claims claims = validator.validate(tokenExpiringIn(3600));

        assertThat(claims.getSubject()).isEqualTo("11111111-1111-1111-1111-111111111111");
        assertThat(claims.get("email", String.class)).isEqualTo("ana@example.com");
    }

    @Test
    void validate_withExpiredToken_throws() {
        JwtValidator validator = validatorWithSecret(SECRET);

        assertThatThrownBy(() -> validator.validate(tokenExpiringIn(-10)))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void validate_withWrongSecret_throws() {
        JwtValidator validator = validatorWithSecret("outro-segredo-com-pelo-menos-32-bytes-de-tamanho");

        assertThatThrownBy(() -> validator.validate(tokenExpiringIn(3600)))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void validate_withMalformedToken_throws() {
        JwtValidator validator = validatorWithSecret(SECRET);

        assertThatThrownBy(() -> validator.validate("nao-e-um-jwt"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void constructor_withTooShortSecret_throwsIllegalStateException() {
        assertThatThrownBy(() -> validatorWithSecret("curto"))
                .isInstanceOf(IllegalStateException.class);
    }
}
