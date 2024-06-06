package com.ecritic.ecritic_application_gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Slf4j
class CustomGlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public CustomGlobalExceptionHandler(final ErrorAttributes errorAttributes,
                                        final WebProperties.Resources resources,
                                        final ApplicationContext applicationContext,
                                        final ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        setMessageReaders(configurer.getReaders());
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
        Throwable throwable = getError(request);
        HttpStatusCode httpStatus = determineHttpStatus(throwable);
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, options);

        MultiValueMap<String, String> headersMap = getResponseHeaders(request);

        Consumer<HttpHeaders> headersConsumer = headers -> {
            headers.addAll(headersMap);
        };

        errorPropertiesMap.remove("requestId");
        errorPropertiesMap.remove("error");

        errorPropertiesMap.put("status", httpStatus.value());
        errorPropertiesMap.put("message", ErrorReponses.getCorrespondingMessage(httpStatus));

        logRequestError(errorPropertiesMap, headersMap);

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private HttpStatusCode determineHttpStatus(Throwable throwable) {
        if (throwable instanceof ResponseStatusException) {
            return ((ResponseStatusException) throwable).getStatusCode();
        } else if (throwable instanceof ConnectException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private MultiValueMap<String, String> getResponseHeaders(ServerRequest request) {
        String requestId = request.headers().firstHeader("X-Request-Id");

        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("X-Request-Id", requestId);

        return headersMap;
    }

    private void logRequestError(Map<String, Object> errorPropertiesMap, MultiValueMap<String, String> headers) {
        log.error("REQUEST INFO: [{}] | Headers: [{}]", errorPropertiesMap, headers);
    }
}
