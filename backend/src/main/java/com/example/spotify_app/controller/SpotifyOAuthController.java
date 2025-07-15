package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.service.TokenStore;
import com.example.spotify_app.service.UserIdGenerator;
import com.example.spotify_app.model.SpotifyTokenResponse;

@RestController
@RequestMapping("/auth")
public class SpotifyOAuthController {
    private final TokenStore tokenStore;
    private final SpotifyConfig spotifyConfig;
    private final UserIdGenerator userIdGenerator;

    public SpotifyOAuthController(TokenStore tokenStore,
            SpotifyConfig spotifyConfig, UserIdGenerator userIdGenerator) {
        this.spotifyConfig = spotifyConfig;
        this.tokenStore = tokenStore;
        this.userIdGenerator = userIdGenerator;
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
                String generatedUserId = userIdGenerator.generateUserId();

                tokenStore.saveToken(generatedUserId, tokenBody);

                String redirectUrl = "http://localhost:9090/auth/callback?user_id=" + generatedUserId;
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            } else {
                String errorUrl = "http://localhost:9090/login?error=failed_to_retrieve_userId";
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

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        String userId = authorization.replace("Bearer ", "");
        tokenStore.removeToken(userId);
        return ResponseEntity.ok().build();
    }
}