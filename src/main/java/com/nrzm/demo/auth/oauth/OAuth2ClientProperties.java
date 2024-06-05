package com.nrzm.demo.auth.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuth2ClientProperties {

    private Map<String, Registration> registration = new HashMap<>();
    private Map<String, Provider> provider = new HashMap<>();

    // Getters and setters

    public Map<String, Registration> getRegistration() {
        return registration;
    }

    public void setRegistration(Map<String, Registration> registration) {
        this.registration = registration;
    }

    public Map<String, Provider> getProvider() {
        return provider;
    }

    public void setProvider(Map<String, Provider> provider) {
        this.provider = provider;
    }

    @Getter
    @Setter
    public static class Registration {
        private String clientId;
        private String clientSecret;
        private String scope;
        private String redirectUri;
        private String authorizationGrantType;
        private String clientName;
    }

    @Getter
    @Setter
    public static class Provider {
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String userNameAttribute;
    }
}
