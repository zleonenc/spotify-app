package com.example.spotify_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
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

    @Value("${frontend.url}")
    private String frontendUrl;

}
