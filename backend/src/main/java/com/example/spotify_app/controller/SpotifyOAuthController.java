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
import com.example.spotify_app.service.TokenService;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class SpotifyOAuthController {
    private final TokenStore tokenStore;
    private final TokenService tokenService;
    private final SpotifyConfig spotifyConfig;

    public SpotifyOAuthController(TokenStore tokenStore, TokenService tokenService, SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
        this.tokenStore = tokenStore;
        this.tokenService = tokenService;
    }

    @GetMapping("/spotify")
    public ResponseEntity<Void> SpotifyOAuth() {
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
    public ResponseEntity<Void> SpotifyCallback(@RequestParam("code") String code) {
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
                String redirectUrl = "http://localhost:9090/auth/callback?access_token=" + tokenBody.getAccessToken();
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            } else {
                String errorUrl = "http://localhost:9090/login?error=failed_to_retrieve_token";
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(errorUrl))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorUrl = "http://localhost:9090/login?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(errorUrl))
                    .build();
        }
    }

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

    @GetMapping("/spotify/refresh")
    public ResponseEntity<SpotifyTokenResponse> refreshToken(@RequestParam String userId) {
        boolean refreshed = tokenService.refreshToken(userId);
        if (refreshed) {
            SpotifyTokenResponse tokenResponse = tokenStore.getToken(userId);
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        tokenStore.removeToken(spotifyConfig.getUserIdDev());
        return ResponseEntity.ok().build();
    }
}