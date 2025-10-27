package com.rishab99058.linkedIn.api_gateway.filters;

import com.rishab99058.linkedIn.api_gateway.config.PublicRouteConfig;
import com.rishab99058.linkedIn.api_gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

    private final JwtUtil jwtUtils;

    public AuthenticationFilter(JwtUtil jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Login Request {}", exchange.getRequest().getURI());
            String path = exchange.getRequest().getPath().value();

            if (PublicRouteConfig.EXACT_URLS.contains(path)) {
                log.debug("Public exact match: " + path + ", skipping authentication");
                return chain.filter(exchange);
            }

            if (PublicRouteConfig.REGEX_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(path).matches())) {
                log.debug("Public regex match: " + path + ", skipping authentication");
                return chain.filter(exchange);
            }

            final String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("Authorization header is missing");
                return exchange.getResponse().setComplete();
            }

            String tokenValue = token.substring(7);

            String userId = jwtUtils.getUserIdFromJwtToken(tokenValue);
            String userEmail = jwtUtils.getEmailFromJwtToken(tokenValue);

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-email", userEmail)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();

            return chain.filter(mutatedExchange);

        };
    }


    public static class Config{}
}
