package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.example.spotify_app.model.Profile.SpotifyProfileResponse;
import com.example.spotify_app.model.Profile.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Profile.SpotifyTopTracksResponse;
import com.example.spotify_app.service.SpotifyProfileService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api/me")
@Validated
public class SpotifyProfileController {
    private final SpotifyProfileService profileService;

    public SpotifyProfileController(SpotifyProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<SpotifyProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {

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
            return profileService.getProfile(userId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top/artists")
    public ResponseEntity<SpotifyTopArtistsResponse> getTopArtists(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") @Min(value = 1, message = "Limit must be at least 1") @Max(value = 50, message = "Limit cannot exceed 50") Integer limit) {

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
            return profileService.getTopArtists(userId, limit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top/tracks")
    public ResponseEntity<SpotifyTopTracksResponse> getTopTracks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") @Min(value = 1, message = "Limit must be at least 1") @Max(value = 50, message = "Limit cannot exceed 50") Integer limit) {

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
            return profileService.getTopTracks(userId, limit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}