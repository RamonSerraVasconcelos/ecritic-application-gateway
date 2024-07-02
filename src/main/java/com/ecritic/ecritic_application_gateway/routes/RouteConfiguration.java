package com.ecritic.ecritic_application_gateway.routes;

import com.ecritic.ecritic_application_gateway.filter.AuthorizationFilter;
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

    private final AuthorizationFilter authorizationFilter;

    @Autowired
    private final ServiceProperties serviceProperties;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        final String USERS_SERVICE = serviceProperties.getEcriticUsersServiceAddress();
        final String AUTHENTICATION_SERVICE = serviceProperties.getEcriticAuthenticationServiceAddress();

        return builder.routes()
                .route("register", r -> r.path("/users")
                        .and()
                        .method(HttpMethod.POST)
                        .uri(USERS_SERVICE + "/users"))

                .route("login", r -> r.path("/auth/login")
                        .and()
                        .method(HttpMethod.POST)
                        .uri(AUTHENTICATION_SERVICE + "/auth/login"))

                .route("login", r -> r.path("/auth/token")
                        .and()
                        .method(HttpMethod.POST)
                        .uri(AUTHENTICATION_SERVICE + "/auth/token"))

                .route("change-email", r -> r.path("/users/{userId}/change-email")
                        .filters(f -> f.rewritePath("/users/(?<userId>.*)/change-email", "/users/${userId}/change-email"))
                        .uri(USERS_SERVICE))

                .route("forgot-password", r -> r.path("/users/forgot-password")
                        .uri(USERS_SERVICE + "/users/forgot-password"))

                .route("reset-password", r -> r.path("/users/{userId}/reset-password")
                        .filters(f -> f.rewritePath("/users/(?<userId>.*)/reset-password", "/users/${userId}/reset-password"))
                        .uri(USERS_SERVICE))

                .route("users", r -> r.path("/users")
                        .filters(f -> f.filter(authorizationFilter))
                        .uri(USERS_SERVICE + "/users"))

                .route("edit users", r -> r.path("/users/**")
                        .filters(f -> f.filter(authorizationFilter))
                        .uri(USERS_SERVICE + "/users"))

                .build();
    }
}
