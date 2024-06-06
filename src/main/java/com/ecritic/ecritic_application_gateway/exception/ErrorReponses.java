package com.ecritic.ecritic_application_gateway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorReponses {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "The requested service is currently unavailable");

    private final HttpStatus httpStatus;
    private final String message;

    public static String getCorrespondingMessage(HttpStatusCode httpStatus) {
        return Arrays.stream(values())
                .filter(errorResponse -> errorResponse.getHttpStatus().equals(httpStatus))
                .map(ErrorReponses::getMessage)
                .findFirst()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
