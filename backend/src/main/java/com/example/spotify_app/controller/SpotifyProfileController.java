package com.example.spotify_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/me")
public class SpotifyProfileController {
    private final TokenStore tokenStore;
    private final SpotifyConfig spotifyConfig;
    private final TokenService tokenService;

    public SpotifyProfileController(TokenStore tokenStore, SpotifyConfig spotifyConfig, TokenService tokenService) {
        this.tokenService = tokenService;
        this.tokenStore = tokenStore;
        this.spotifyConfig = spotifyConfig;
    }

    @GetMapping("/top/artists")
    public ResponseEntity<String> getTopArtists() {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(spotifyConfig.getUserIdDev());

        if (tokenResponse == null || tokenResponse.getAccessToken().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String accessToken = tokenResponse.getAccessToken();
        ResponseEntity<String> response = requestTopArtists(accessToken);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            boolean refreshed = tokenService.refreshToken(spotifyConfig.getUserIdDev());
            if (refreshed) {
                tokenResponse = tokenStore.getToken(spotifyConfig.getUserIdDev());
                accessToken = tokenResponse.getAccessToken();
                response = requestTopArtists(accessToken);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching top artists");
        }
    }

    private ResponseEntity<String> requestTopArtists(String accessToken) {
        RestClient restClient = RestClient.create();

        try {
            String response = restClient.get()
                .uri(spotifyConfig.getApiUrl() + "/me/top/artists")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(String.class);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching top artists");
        }
    }   
}
