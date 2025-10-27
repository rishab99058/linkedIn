package com.rishab99058.linkedIn.api_gateway.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class PublicRouteConfig {

    public static final List<String> EXACT_URLS = List.of(
            "/health",
            "/api/status",
            "/welcome"

    );

    public static final List<Pattern> REGEX_PATTERNS = List.of(
            Pattern.compile("/users/auth/.*"),
            Pattern.compile("/public/.*"),
            Pattern.compile("/swagger-ui/.*"),
            Pattern.compile("/v3/api-docs/.*")

    );



}
