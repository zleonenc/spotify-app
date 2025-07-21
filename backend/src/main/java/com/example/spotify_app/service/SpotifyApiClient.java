package com.example.spotify_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.config.RetryConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.util.RetryUtils;

@Service
public class SpotifyApiClient {
    private final SpotifyConfig spotifyConfig;
    private final RetryConfig retryConfig;
    private final TokenStore tokenStore;
    private final RetryUtils retryUtils;
    private final SpotifyOAuthService oauthService;
    private final RestClient restClient;

    public SpotifyApiClient(TokenStore tokenStore, SpotifyConfig spotifyConfig, RetryConfig retryConfig, RetryUtils retryUtils,
            SpotifyOAuthService oauthService) {
        this.spotifyConfig = spotifyConfig;
        this.retryConfig = retryConfig;
        this.retryUtils = retryUtils;
        this.tokenStore = tokenStore;
        this.oauthService = oauthService;
        this.restClient = RestClient.create();
    }

    public String getValidAccessToken(String userId) {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null
                || tokenResponse.getAccessToken().isEmpty()) {
            return null;
        }

        return tokenResponse.getAccessToken();
    }

    private <T> ResponseEntity<T> executeRequest(String apiEndpoint, String accessToken, Class<T> responseType) {
        int attempts = 0;

        while (attempts < retryConfig.getMaxRetryAttempts()) {
            try {
                String fullUrl = spotifyConfig.getApiUrl() + apiEndpoint;

                T responseBody = restClient.get()
                        .uri(fullUrl)
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .body(responseType);

                return ResponseEntity.ok(responseBody);

            } catch (HttpClientErrorException.TooManyRequests e) {
                attempts++;

                if (attempts >= retryConfig.getMaxRetryAttempts()) {
                    System.err.println("Max retry attempts reached for request: " + apiEndpoint);
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
                }

                long retryAfterSeconds = retryUtils.getRetryAfterSeconds(e);
                System.out.println("(429) Attempt:" + attempts + " for: " + apiEndpoint + ". Retrying after "
                        + retryAfterSeconds + " seconds.");

                try {
                    Thread.sleep(retryAfterSeconds * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
                }

            } catch (HttpClientErrorException.Unauthorized e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } catch (HttpClientErrorException e) {
                System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getMessage());
                return ResponseEntity.status(e.getStatusCode()).build();
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    public <T> ResponseEntity<T> makeRequest(String userId, String apiEndpoint, Class<T> responseType) {
        String accessToken = getValidAccessToken(userId);

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResponseEntity<T> response = executeRequest(apiEndpoint, accessToken, responseType);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            if (oauthService.refreshToken(userId)) {
                String newAccessToken = getValidAccessToken(userId);
                if (newAccessToken != null && !newAccessToken.equals(accessToken)) {
                    response = executeRequest(apiEndpoint, newAccessToken, responseType);
                }
            }
        }

        return response;
    }
}
