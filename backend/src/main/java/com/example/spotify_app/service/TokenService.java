package com.example.spotify_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;

@Service
public class TokenService {
    private final TokenStore tokenStore;
    private final SpotifyConfig spotifyConfig;

    public TokenService(TokenStore tokenStore, SpotifyConfig spotifyConfig) {
        this.tokenStore = tokenStore;
        this.spotifyConfig = spotifyConfig;
    }

    public boolean refreshToken(String userId) {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);

        if (tokenResponse == null || tokenResponse.getAccessToken().isEmpty()) {
            return false;
        }

        RestClient restClient = RestClient.create();
        String auth = spotifyConfig.getClientId() + ":" + spotifyConfig.getClientSecret();
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());

        String body = "grant_type=refresh_token&refresh_token=" + tokenResponse.getRefreshToken();

        try {

            SpotifyTokenResponse newTokenBody = restClient.post()
                .uri(spotifyConfig.getTokenUrl())
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(SpotifyTokenResponse.class);

            if (newTokenBody != null && newTokenBody.getAccessToken() != null) {
                tokenStore.saveToken(userId, newTokenBody);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
