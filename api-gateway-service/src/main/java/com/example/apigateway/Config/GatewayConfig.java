package com.example.apigateway.Config;

import com.example.apigateway.Filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))

                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))

                .build();
    }

}
