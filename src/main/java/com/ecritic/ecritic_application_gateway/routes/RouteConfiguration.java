package com.ecritic.ecritic_application_gateway.routes;

import com.ecritic.ecritic_application_gateway.properties.ServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class RouteConfiguration {

    @Autowired
    private ServiceProperties serviceProperties;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        final String USERS_SERVICE = serviceProperties.getEcriticUsersServiceAddress();

        return builder.routes()
                .route("register", r -> r.path("/users")
                        .and()
                        .method(HttpMethod.POST)
                        .uri(USERS_SERVICE + "/users"))

                .route("users", r -> r.path("/users")
                        .uri(USERS_SERVICE + "/users"))

                .route("edit users", r -> r.path("/users/**")
                        .uri(USERS_SERVICE + "/users"))

                .route("forgot-password", r -> r.path("/users/forgot-password")
                        .uri(USERS_SERVICE + "/users/forgot-password"))

                .route("reset-password", r -> r.path("/reset-password")
                        .uri(USERS_SERVICE + "/users/reset-password"))

                .route("reset-email", r -> r.path("/reset-email")
                        .uri(USERS_SERVICE + "/users/reset-email"))
                .build();
    }
}
