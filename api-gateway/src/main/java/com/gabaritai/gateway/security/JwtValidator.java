package com.gabaritai.gateway.security;

import com.gabaritai.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/** Valida a assinatura dos tokens emitidos pelo auth-service (RNF-026, RNF-009). */
@Component
public class JwtValidator {

    private static final int MIN_SECRET_BYTES = 32;

    private final SecretKey signingKey;

    public JwtValidator(JwtProperties properties) {
        byte[] secretBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "app.jwt.secret (JWT_SECRET) deve ter pelo menos 32 bytes para validacao HS256.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * @throws JwtException se o token estiver ausente, malformado, expirado ou com assinatura invalida.
     */
    public Claims validate(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
