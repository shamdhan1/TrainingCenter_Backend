package com.trainingcenter.gateway.filter;

import com.trainingcenter.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/validate",
            "/v3/api-docs",
            "/swagger-ui"
    );

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Check if endpoint is unsecured
            boolean isSecured = OPEN_API_ENDPOINTS.stream().noneMatch(path::startsWith);

            if (isSecured) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "Invalid Authorization Header Format", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);
                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Unauthorized Access: Invalid or Expired Token", HttpStatus.UNAUTHORIZED);
                }

                try {
                    Claims claims = jwtUtil.getAllClaimsFromToken(token);
                    String username = claims.getSubject();
                    Object userId = claims.get("userId");
                    Object roles = claims.get("roles");

                    // Mutate request by attaching authenticated headers downstream
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Name", username != null ? username : "")
                            .header("X-User-Id", userId != null ? String.valueOf(userId) : "")
                            .header("X-User-Roles", roles != null ? String.valueOf(roles) : "")
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                } catch (Exception e) {
                    return onError(exchange, "Error parsing JWT Token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"success\":false,\"message\":\"" + err + "\",\"status\":" + httpStatus.value() + "}";
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    public static class Config {
        // Configuration properties if needed
    }
}
