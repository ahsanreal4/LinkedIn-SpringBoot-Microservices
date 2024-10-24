package com.linkedIn.api_gateway.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final String ADMIN_KEY = "x-user-admin";
    private final String ID_KEY = "x-user-id";
    private final String EMAIL_KEY = "x-user-email";

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isValidHeaders(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();

        if (headers.containsKey(ADMIN_KEY) || headers.containsKey(ID_KEY) || headers.containsKey(EMAIL_KEY)) return false;

        return true;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);


        if (token != null && jwtUtil.validateToken(token)) {
            ServerHttpRequest request = exchange.getRequest();

            // If security breach in headers
            if(!isValidHeaders(request)) return chain.filter(exchange);

            Claims claims = jwtUtil.extractClaims(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, null);

            // Set headers to forward claims downstream
            request.mutate()
                    .header(EMAIL_KEY, claims.get(EMAIL_KEY, String.class))
                    .header(ADMIN_KEY, String.valueOf(claims.get(ADMIN_KEY, Boolean.class)))
                    .header(ID_KEY, String.valueOf(claims.get(ID_KEY, Long.class)));

            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
