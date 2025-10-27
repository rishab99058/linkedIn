package com.rishab99058.linkedIn.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingUserFilter extends AbstractGatewayFilterFactory<LoggingUserFilter.Config> {

    public LoggingUserFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Logging from Service based {} - Method: {} - Content-Type: {}", 
                request.getURI(), 
                request.getMethod(), 
                request.getHeaders().getFirst("Content-Type"));
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Logging from Service based post {} - Status: {}", 
                    request.getURI(), 
                    exchange.getResponse().getStatusCode());
            }));
        });
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}
