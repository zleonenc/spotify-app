package com.example.spotify_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.service.TokenStore;
import com.example.spotify_app.service.TokenService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/me")
public class SpotifyProfileController {
    private final TokenStore tokenStore;
    private final SpotifyConfig spotifyConfig;
    private final TokenService tokenService;

    public SpotifyProfileController(TokenStore tokenStore, SpotifyConfig spotifyConfig, TokenService tokenService) {
        this.tokenService = tokenService;
        this.tokenStore = tokenStore;
        this.spotifyConfig = spotifyConfig;
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String authHeader) {
        String userToken = extractToken(authHeader);
        if (userToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        ResponseEntity<String> response = requestProfile(userToken);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            boolean refreshed = tokenService.refreshToken(spotifyConfig.getUserIdDev());
            if (refreshed) {
                SpotifyTokenResponse tokenResponse = tokenStore.getToken(spotifyConfig.getUserIdDev());
                response = requestProfile(tokenResponse.getAccessToken());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - please re-authenticate");
            }
        }

        return response;
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private ResponseEntity<String> requestProfile(String accessToken) {
        RestClient restClient = RestClient.create();

        try {
            String response = restClient.get()
                    .uri(spotifyConfig.getApiUrl() + "/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            if (e.getMessage().contains("401")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching profile");
        }
    }
}
