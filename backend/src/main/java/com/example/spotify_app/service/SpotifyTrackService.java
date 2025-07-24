package com.example.spotify_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.spotify_app.model.Track.Track;

@Service
public class SpotifyTrackService {

    private final SpotifyApiClient tokenService;

    public SpotifyTrackService(SpotifyApiClient tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseEntity<Track> getTrackById(String userId, String trackId) {
        String endpoint = String.format("/tracks/%s", trackId);
        return tokenService.makeRequest(userId, endpoint, Track.class);
    }
}
