package com.example.spotify_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;

@Service
public class SpotifyApiClient {
    private final SpotifyConfig spotifyConfig;
    private final TokenStore tokenStore;
    private final SpotifyOAuthService oauthService;
    private final RestClient restClient;

    public SpotifyApiClient(TokenStore tokenStore, SpotifyConfig spotifyConfig, SpotifyOAuthService oauthService,
            RestClient restClient) {
        this.spotifyConfig = spotifyConfig;
        this.tokenStore = tokenStore;
        this.oauthService = oauthService;
        this.restClient = restClient;
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
        String fullUrl = spotifyConfig.getApiUrl() + apiEndpoint;

        try {
            T responseBody = restClient.get()
                    .uri(fullUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(responseType);

            return ResponseEntity.ok(responseBody);

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
