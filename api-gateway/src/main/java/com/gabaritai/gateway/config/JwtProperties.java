package com.gabaritai.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Deve usar o mesmo segredo configurado no auth-service (RNF-026), que assina os tokens. */
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
