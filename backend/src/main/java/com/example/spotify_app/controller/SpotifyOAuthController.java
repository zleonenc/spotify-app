package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.client.RestClient;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.service.TokenStore;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class SpotifyOAuthController {
    private final TokenStore tokenStore;
    private final SpotifyConfig spotifyConfig;

    public SpotifyOAuthController(TokenStore tokenStore, SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
        this.tokenStore = tokenStore;
    }

    @GetMapping("/spotify")
    public ResponseEntity<Void> SpotifyOAuth(){
        String url = spotifyConfig.getAuthorizeUrl() +
                "?client_id=" + spotifyConfig.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(spotifyConfig.getRedirectUri(), StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(spotifyConfig.getScopes(), StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/spotify/callback")
    public ResponseEntity<String> SpotifyCallback(@RequestParam("code") String code) {
        RestClient restClient = RestClient.create();

        String auth = spotifyConfig.getClientId() + ":" + spotifyConfig.getClientSecret();
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());

        String body = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + spotifyConfig.getRedirectUri();

        try {
            SpotifyTokenResponse tokenBody = restClient.post()
                .uri(spotifyConfig.getTokenUrl())
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .body(SpotifyTokenResponse.class);

            if (tokenBody != null) {
                tokenStore.saveToken(spotifyConfig.getUserIdDev(), tokenBody);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tokenBody.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to retrieve token from Spotify.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Spotify OAuth callback: " + e.getMessage());
        }
    }

    // Endpoint to retrieve the Spotify token, using an ID
    @GetMapping("/spotify/token")
    public ResponseEntity<SpotifyTokenResponse> getSpotifyTokenByUserId(@RequestParam String userId) {
        SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);
        if (tokenResponse != null) {
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

}