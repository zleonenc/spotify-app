package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.model.Track.Track;
import com.example.spotify_app.service.TokenService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifyTrackController {

    private final TokenService tokenService;

    public SpotifyTrackController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<Track> getTrackById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String trackId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = String.format("/tracks/%s", trackId);

        return tokenService.makeRequest(userId, endpoint, Track.class);
    }
}