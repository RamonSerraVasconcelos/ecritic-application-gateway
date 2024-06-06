package com.ecritic.ecritic_application_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("REQUEST INFO:" +
                        " | Client IP Address - [{}]" +
                        " | Request - [{}]" +
                        " | Query params: [{}]" +
                        " | Headers: [{}]",
                Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress(),
                exchange.getAttribute(GATEWAY_ROUTE_ATTR),
                exchange.getRequest().getQueryParams(),
                exchange.getRequest().getHeaders());

        return chain.filter(exchange);
    }
}

