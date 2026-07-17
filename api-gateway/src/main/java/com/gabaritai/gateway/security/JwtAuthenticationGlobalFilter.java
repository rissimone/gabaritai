package com.gabaritai.gateway.security;

import com.gabaritai.gateway.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Autenticacao inicial no Gateway (RNF-009): exige um JWT valido em toda rota que nao esteja
 * na lista de caminhos publicos. Repassa a identidade do usuario autenticado aos servicos internos
 * via cabecalhos X-User-Id / X-User-Email — os servicos internos nao ficam expostos publicamente
 * (RNF-018/019), portanto confiam nesses cabecalhos vindos apenas do Gateway.
 */
@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private final JwtValidator jwtValidator;
    private final SecurityProperties securityProperties;

    public JwtAuthenticationGlobalFilter(JwtValidator jwtValidator, SecurityProperties securityProperties) {
        this.jwtValidator = jwtValidator;
        this.securityProperties = securityProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return reject(exchange, "Token de acesso ausente.");
        }

        String token = authHeader.substring("Bearer ".length());
        Claims claims;
        try {
            claims = jwtValidator.validate(token);
        } catch (JwtException | IllegalArgumentException e) {
            return reject(exchange, "Token de acesso invalido ou expirado.");
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", claims.getSubject())
                .header("X-User-Email", claims.get("email", String.class))
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublic(String path) {
        return securityProperties.getPublicPaths().stream().anyMatch(path::startsWith);
    }

    private Mono<Void> reject(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = """
                {"errorCode":"UNAUTHORIZED","message":"%s"}""".formatted(message);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
