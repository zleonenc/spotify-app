package com.example.spotify_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.config.RetryConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.util.RetryUtils;
import com.example.spotify_app.util.UserIdGenerator;

@Service
public class SpotifyOAuthService {

    private final SpotifyConfig spotifyConfig;
    private final RetryConfig retryConfig;
    private final RetryUtils retryUtils;
    private final TokenStore tokenStore;

    public SpotifyOAuthService(SpotifyConfig spotifyConfig, RetryConfig retryConfig, RetryUtils retryUtils,
            TokenStore tokenStore) {
        this.spotifyConfig = spotifyConfig;
        this.retryConfig = retryConfig;
        this.retryUtils = retryUtils;
        this.tokenStore = tokenStore;
    }

    public String buildAuthorizationUrl() {
        return spotifyConfig.getAuthorizeUrl() +
                "?client_id=" + spotifyConfig.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(spotifyConfig.getRedirectUri(), StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(spotifyConfig.getScopes(), StandardCharsets.UTF_8);
    }

    public String buildSuccessRedirectUrl(String userId) {
        return spotifyConfig.getFrontendUrl() + "/auth/callback?user_id=" + userId;
    }

    public String buildErrorRedirectUrl(String errorMessage) {
        return spotifyConfig.getFrontendUrl() + "/login?error="
                + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
    }

    private String encodeClientCredentials() {
        String auth = spotifyConfig.getClientId() + ":" + spotifyConfig.getClientSecret();
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private SpotifyTokenResponse requestTokenFromSpotify(String requestBody) throws Exception {
        RestClient restClient = RestClient.create();
        String encodedAuth = encodeClientCredentials();

        int attempts = 0;

        while (attempts < retryConfig.getMaxRetryAttempts()) {
            try {
                return restClient.post()
                        .uri(spotifyConfig.getTokenUrl())
                        .header("Authorization", "Basic " + encodedAuth)
                        .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(requestBody)
                        .retrieve()
                        .body(SpotifyTokenResponse.class);

            } catch (HttpClientErrorException.TooManyRequests e) {
                attempts++;

                if (attempts >= retryConfig.getMaxRetryAttempts()) {
                    throw new RuntimeException("Max retry attempts reached for token request: "
                            + retryConfig.getMaxRetryAttempts() + " attempts", e);
                }

                long retryAfterSeconds = retryUtils.getRetryAfterSeconds(e);
                System.out.println("(429) Attempt: " + attempts + " for token exchange. Retrying after "
                        + retryAfterSeconds + " seconds.");

                try {
                    Thread.sleep(retryAfterSeconds * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted: ", ie);
                }
            }
        }

        throw new RuntimeException("Token request failed after " + retryConfig.getMaxRetryAttempts() + " attempts");
    }

    public String exchangeCodeForToken(String code) throws Exception {
        String requestBody = "grant_type=authorization_code&code=" + code + "&redirect_uri="
                + spotifyConfig.getRedirectUri();

        try {
            SpotifyTokenResponse tokenResponse = requestTokenFromSpotify(requestBody);

            if (tokenResponse != null) {
                String userId = UserIdGenerator.generateUserId();
                tokenStore.saveToken(userId, tokenResponse);
                return userId;
            } else {
                throw new RuntimeException("Failed to retrieve token from Spotify");
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean refreshToken(String userId) {
        SpotifyTokenResponse token = tokenStore.getToken(userId);

        if (token == null || token.getRefreshToken() == null
                || token.getRefreshToken().isEmpty()) {
            return false;
        }

        String requestBody = "grant_type=refresh_token&refresh_token=" + token.getRefreshToken();

        try {
            SpotifyTokenResponse newTokenBody = requestTokenFromSpotify(requestBody);

            if (newTokenBody != null && newTokenBody.getAccessToken() != null) {
                if (newTokenBody.getRefreshToken() == null) {
                    newTokenBody.setRefreshToken(token.getRefreshToken());
                }
                tokenStore.saveToken(userId, newTokenBody);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout(String userId) {
        tokenStore.removeToken(userId);
    }
}
