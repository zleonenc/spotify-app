package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyArtistsResponse;
import com.example.spotify_app.service.TokenService;

@RestController
@RequestMapping("/api")
public class SpotifyArtistController {

    private final TokenService tokenService;
    private final SpotifyConfig spotifyConfig;

    public SpotifyArtistController(TokenService tokenService, SpotifyConfig spotifyConfig) {
        this.tokenService = tokenService;
        this.spotifyConfig = spotifyConfig;
    }

    @GetMapping("/me/top/artists")
    public ResponseEntity<SpotifyArtistsResponse> getTopArtists(@RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        RestClient restClient = RestClient.create();

        try {
            SpotifyArtistsResponse response = restClient.get()
                    .uri(spotifyConfig.getApiUrl() + "/me/top/artists?limit=20")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(SpotifyArtistsResponse.class);

            return ResponseEntity.ok(response);

        } catch (HttpClientErrorException.Unauthorized e) {
            boolean refreshed = tokenService.refreshToken(spotifyConfig.getUserIdDev());

            if (refreshed) {
                String newAccessToken = tokenService.getValidAccessToken(spotifyConfig.getUserIdDev());

                if (newAccessToken != null) {
                    try {
                        SpotifyArtistsResponse response = restClient.get()
                                .uri(spotifyConfig.getApiUrl() + "/me/top/artists?limit=20")
                                .header("Authorization", "Bearer " + newAccessToken)
                                .retrieve()
                                .body(SpotifyArtistsResponse.class);

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

    private String extractAccessToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
