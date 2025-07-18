package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.Me.SpotifyProfileResponse;
import com.example.spotify_app.model.Me.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Me.SpotifyTopTracksResponse;
import com.example.spotify_app.service.TokenService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api/me")
public class SpotifyProfileController {
    private final TokenService tokenService;

    public SpotifyProfileController(SpotifyConfig spotifyConfig, TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/profile")
    public ResponseEntity<SpotifyProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = "/me";

        return tokenService.makeRequest(userId, endpoint, SpotifyProfileResponse.class);
    }

    @GetMapping("/top/artists")
    public ResponseEntity<SpotifyTopArtistsResponse> getTopArtists(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = String.format("/me/top/artists?limit=%d", limit);

        return tokenService.makeRequest(userId, endpoint, SpotifyTopArtistsResponse.class);
    }

    @GetMapping("/top/tracks")
    public ResponseEntity<SpotifyTopTracksResponse> getTopTracks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = String.format("/me/top/tracks?limit=%d", limit);

        return tokenService.makeRequest(userId, endpoint, SpotifyTopTracksResponse.class);
    }
}