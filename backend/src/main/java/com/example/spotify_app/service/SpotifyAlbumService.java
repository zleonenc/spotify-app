package com.example.spotify_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Album.AlbumTracksResponse;

@Service
public class SpotifyAlbumService {

    private final SpotifyApiClient tokenService;

    public SpotifyAlbumService(SpotifyApiClient tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseEntity<Album> getAlbumById(String userId, String albumId) {
        String endpoint = String.format("/albums/%s", albumId);
        return tokenService.makeRequest(userId, endpoint, Album.class);
    }

    public ResponseEntity<AlbumTracksResponse> getAlbumTracks(String userId, String albumId) {
        String endpoint = String.format("/albums/%s/tracks", albumId);
        return tokenService.makeRequest(userId, endpoint, AlbumTracksResponse.class);
    }
}
