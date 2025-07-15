package com.example.spotify_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    public String getValidAccessToken(String userId) {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null
                || tokenResponse.getAccessToken().isEmpty()) {
            return null;
        }

        return tokenResponse.getAccessToken();
    }

    public boolean refreshToken(String userId) {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);

        if (tokenResponse == null || tokenResponse.getRefreshToken() == null
                || tokenResponse.getRefreshToken().isEmpty()) {
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
                if (newTokenBody.getRefreshToken() == null) {
                    newTokenBody.setRefreshToken(tokenResponse.getRefreshToken());
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

    public <T> ResponseEntity<T> makeRequest(String userId, String apiEndpoint, Class<T> responseType) {
        String accessToken = getValidAccessToken(userId);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RestClient restClient = RestClient.create();
        String fullUrl = spotifyConfig.getApiUrl() + apiEndpoint;

        try {
            T response = restClient.get()
                    .uri(fullUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(responseType);

            return ResponseEntity.ok(response);

        } catch (HttpClientErrorException.Unauthorized e) {
            boolean refreshed = refreshToken(userId);

            if (refreshed) {
                String newAccessToken = getValidAccessToken(userId);
                if (newAccessToken != null) {
                    try {
                        T response = restClient.get()
                                .uri(fullUrl)
                                .header("Authorization", "Bearer " + newAccessToken)
                                .retrieve()
                                .body(responseType);

                        return ResponseEntity.ok(response);
                    } catch (Exception retryException) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
