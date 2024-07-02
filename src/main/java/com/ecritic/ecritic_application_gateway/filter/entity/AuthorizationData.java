package com.ecritic.ecritic_application_gateway.filter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AuthorizationData {

    private String accessToken;

    private String tokenType;

    private Integer expiresIn;
}
