package com.rishab99058.linkedIn.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;;

@Configuration
@Slf4j
public class GlobalLoggingFilters implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Logging from Global {}", exchange.getRequest().getURI());
        return chain.filter(exchange).then(Mono.fromRunnable(()-> {
            log.info("Logging from Global post {}", exchange.getResponse().getStatusCode());
        }));
    }
}
