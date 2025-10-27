package com.rishab99058.linkedIn.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class MultipartHandlingFilter extends AbstractGatewayFilterFactory<MultipartHandlingFilter.Config> {

    public MultipartHandlingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("MultipartHandlingFilter: Processing request to {}", exchange.getRequest().getURI());
            
            String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            log.info("MultipartHandlingFilter: Content-Type: {}", contentType);
            
            if (contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                log.info("MultipartHandlingFilter: Detected multipart request with content-type: {}", contentType);

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .build();
                
                ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();
                
                log.info("MultipartHandlingFilter: Forwarding multipart request to backend service");
                return chain.filter(modifiedExchange);
            } else {
                log.warn("MultipartHandlingFilter: Request does not have multipart content-type. Content-Type: {}", contentType);
            }
            
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}
