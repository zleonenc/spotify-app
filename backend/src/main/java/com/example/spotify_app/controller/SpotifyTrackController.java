package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.spotify_app.model.Track.Track;
import com.example.spotify_app.service.SpotifyTrackService;
import com.example.spotify_app.util.AuthUtils;

@RequestMapping("/api")
@RestController
@Validated
public class SpotifyTrackController {

    private final SpotifyTrackService trackService;

    public SpotifyTrackController(SpotifyTrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<Track> getTrackById(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Track ID must not be empty") @PathVariable("id") String trackId) {

        String userId;
        try {
            userId = AuthUtils.extractUserId(authHeader);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            return trackService.getTrackById(userId, trackId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}