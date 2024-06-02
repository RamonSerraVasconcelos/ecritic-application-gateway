package com.ecritic.ecritic_application_gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.properties.services")
@Getter
@Setter
public class ServiceProperties {

    private String ecriticUsersServiceAddress;
    private String ecriticAuthenticationServiceAddress;
}
