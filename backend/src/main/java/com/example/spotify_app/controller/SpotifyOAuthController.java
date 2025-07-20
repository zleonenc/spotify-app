package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

import com.example.spotify_app.service.SpotifyOAuthService;

@RestController
@RequestMapping("/auth")
public class SpotifyOAuthController {

    private final SpotifyOAuthService oauthService;

    public SpotifyOAuthController(SpotifyOAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/spotify")
    public ResponseEntity<Void> SpotifyOAuth() {
        String authorizationUrl = oauthService.buildAuthorizationUrl();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl))
                .build();
    }

    @GetMapping("/spotify/callback")
    public ResponseEntity<Void> SpotifyCallback(@RequestParam("code") String code) {
        try {
            String userId = oauthService.exchangeCodeForToken(code);
            String redirectUrl = oauthService.buildSuccessRedirectUrl(userId);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            String errorUrl = oauthService.buildErrorRedirectUrl(e.getMessage());

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(errorUrl))
                    .build();
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        String userId = authorization.replace("Bearer ", "");
        oauthService.logout(userId);
        return ResponseEntity.ok().build();
    }
}