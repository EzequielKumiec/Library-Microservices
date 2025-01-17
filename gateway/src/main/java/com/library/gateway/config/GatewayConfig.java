package com.library.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("clientes_route", r -> r.path("/api/personas/**")
                        .uri("http://clientes-service:8081/api/personas/"))  // Cambiado localhost a clientes-service
                .route("biblioteca_route", r -> r.path("/api/biblioteca/**")
                        .uri("http://biblioteca-service:8082"))  // Cambiado localhost a biblioteca-service
                .build();
    }
}
