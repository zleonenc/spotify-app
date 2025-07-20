package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.model.Profile.SpotifyProfileResponse;
import com.example.spotify_app.model.Profile.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Profile.SpotifyTopTracksResponse;
import com.example.spotify_app.service.SpotifyProfileService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api/me")
public class SpotifyProfileController {
    private final SpotifyProfileService profileService;

    public SpotifyProfileController(SpotifyProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<SpotifyProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return profileService.getProfile(userId);
    }

    @GetMapping("/top/artists")
    public ResponseEntity<SpotifyTopArtistsResponse> getTopArtists(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return profileService.getTopArtists(userId, limit);
    }

    @GetMapping("/top/tracks")
    public ResponseEntity<SpotifyTopTracksResponse> getTopTracks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return profileService.getTopTracks(userId, limit);
    }
}