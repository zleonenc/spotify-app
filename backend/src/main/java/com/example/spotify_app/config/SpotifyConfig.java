package com.example.spotify_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpotifyConfig {
    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @Value("${spotify.authorize-url}")
    private String authorizeUrl;

    @Value("${spotify.token-url}")
    private String tokenUrl;

    @Value("${spotify.api-base-url}")
    private String apiUrl;

    @Value("${spotify.scopes}")
    private String scopes;

    @Value("${spotify.user-id.dev}")
    private String userIdDev;

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getRedirectUri() {
        return this.redirectUri;
    }

    public String getAuthorizeUrl() {
        return this.authorizeUrl;
    }

    public String getTokenUrl() {
        return this.tokenUrl;
    }

    public String getApiUrl() {
        return this.apiUrl;
    }

    public String getScopes() {
        return this.scopes;
    }

    public String getUserIdDev() {
        return this.userIdDev;
    }

}
