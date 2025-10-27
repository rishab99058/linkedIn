package com.rishab99058.linkedIn.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;

@Configuration
public class MultipartConfig {

    @Bean
    public WebFilter multipartWebFilter() {
        return (exchange, chain) -> {
            // Log multipart requests
            String contentType = exchange.getRequest().getHeaders().getFirst("Content-Type");
            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                System.out.println("Multipart request detected: " + contentType);
            }
            return chain.filter(exchange);
        };
    }
}
