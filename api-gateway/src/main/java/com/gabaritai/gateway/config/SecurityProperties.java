package com.gabaritai.gateway.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Prefixos de rota que nao exigem token de acesso (RNF-009). */
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private List<String> publicPaths = List.of();

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
