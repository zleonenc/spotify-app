package com.example.spotify_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.spotify_app.model.Me.SpotifyProfileResponse;
import com.example.spotify_app.model.Me.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Me.SpotifyTopTracksResponse;

@Service
public class SpotifyProfileService {

    private final SpotifyApiClient tokenService;

    public SpotifyProfileService(SpotifyApiClient tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseEntity<SpotifyProfileResponse> getProfile(String userId) {
        String endpoint = "/me";
        return tokenService.makeRequest(userId, endpoint, SpotifyProfileResponse.class);
    }

    public ResponseEntity<SpotifyTopArtistsResponse> getTopArtists(String userId, int limit) {
        String endpoint = String.format("/me/top/artists?limit=%d", limit);
        return tokenService.makeRequest(userId, endpoint, SpotifyTopArtistsResponse.class);
    }

    public ResponseEntity<SpotifyTopTracksResponse> getTopTracks(String userId, int limit) {
        String endpoint = String.format("/me/top/tracks?limit=%d", limit);
        return tokenService.makeRequest(userId, endpoint, SpotifyTopTracksResponse.class);
    }
}
