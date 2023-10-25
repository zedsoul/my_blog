package com.zlc.blogencrypt.config;

import com.zlc.blogencrypt.filter.JWTFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/service/**")
                        .filters(f -> f.filter(new JWTFilter()))
                        .uri("lb://blog-server")
                )
                .build();
    }
}

