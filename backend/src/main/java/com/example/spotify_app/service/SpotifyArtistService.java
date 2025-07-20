package com.example.spotify_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;

@Service
public class SpotifyArtistService {

    private final SpotifyApiClient tokenService;

    public SpotifyArtistService(SpotifyApiClient tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseEntity<Artist> getArtistById(String userId, String artistId) {
        String endpoint = String.format("/artists/%s", artistId);
        return tokenService.makeRequest(userId, endpoint, Artist.class);
    }

    public ResponseEntity<ArtistTopTracksResponse> getTopTracks(String userId, String artistId) {
        String endpoint = String.format("/artists/%s/top-tracks", artistId);
        return tokenService.makeRequest(userId, endpoint, ArtistTopTracksResponse.class);
    }

    public ResponseEntity<ArtistAlbumsResponse> getAlbums(String userId, String artistId, Integer limit,
            Integer offset) {
        StringBuilder endpoint = new StringBuilder(String.format("/artists/%s/albums", artistId));
        endpoint.append("?limit=").append(limit);
        endpoint.append("&offset=").append(offset);
        return tokenService.makeRequest(userId, endpoint.toString(), ArtistAlbumsResponse.class);
    }
}
