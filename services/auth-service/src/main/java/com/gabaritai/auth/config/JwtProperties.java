package com.gabaritai.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /** Chave secreta usada para assinar os tokens (HMAC-SHA256). Obrigatoria via variavel de ambiente JWT_SECRET. */
    private String secret;

    /** Tempo de expiracao do access token, em segundos. */
    private long expirationSeconds = 3600;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
