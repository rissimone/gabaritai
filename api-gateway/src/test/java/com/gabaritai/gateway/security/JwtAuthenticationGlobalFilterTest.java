package com.gabaritai.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Testa o filtro global de autenticacao (RNF-009) com uma rota sintetica "no://op" — tecnica
 * padrao do Spring Cloud Gateway para testar filtros sem depender de um backend real.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(JwtAuthenticationGlobalFilterTest.TestRouteConfig.class)
class JwtAuthenticationGlobalFilterTest {

    private static final String SECRET = "test-only-secret-key-must-be-at-least-32-bytes-long";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @TestConfiguration
    static class TestRouteConfig {
        @Bean
        RouteLocator testRoutes(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("protected-test", r -> r.path("/api/v1/protected-test/**").uri("no://op"))
                    .build();
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    private String validToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject("11111111-1111-1111-1111-111111111111")
                .claim("email", "ana@example.com")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(KEY)
                .compact();
    }

    @BeforeEach
    void increaseTimeout() {
        webTestClient = webTestClient.mutate()
                .responseTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }

    @Test
    void protectedRoute_withoutToken_returns401() {
        webTestClient.get().uri("/api/v1/protected-test/algo")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void protectedRoute_withMalformedToken_returns401() {
        webTestClient.get().uri("/api/v1/protected-test/algo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token-invalido")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void protectedRoute_withValidToken_isForwarded() {
        webTestClient.get().uri("/api/v1/protected-test/algo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void publicPath_actuatorHealth_doesNotRequireToken() {
        webTestClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }
}
