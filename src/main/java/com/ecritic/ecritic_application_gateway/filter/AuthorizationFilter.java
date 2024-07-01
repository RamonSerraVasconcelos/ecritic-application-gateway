package com.ecritic.ecritic_application_gateway.filter;

import com.ecritic.ecritic_application_gateway.filter.entity.AuthorizationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.ecritic.ecritic_application_gateway.properties.ServiceProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter implements GatewayFilter {

    private final ObjectMapper objectMapper;

    @Autowired
    private ServiceProperties servicesProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = extractToken(exchange);

        if (accessToken == null) {
            log.warn("Authorization token not found in request headers");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(servicesProperties.getEcriticAuthenticationServiceAddress())
                .build();

        return webClient.post()
                .uri("/auth/token/introspect")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(AuthorizationData.class)
                .flatMap(authorizationDataResponse -> {
                    exchange.getRequest().mutate()
                            .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, authorizationDataResponse.getAccessToken()));

                    log.info("Retrieved authorization data from ecritic-authentication-service: [{}]", authorizationDataResponse);

                    return chain.filter(exchange);
                })
                .onErrorResume(throwable -> {
                    log.error("Failed to retrieve authorization data from ecritic-authentication-service", throwable);

                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
