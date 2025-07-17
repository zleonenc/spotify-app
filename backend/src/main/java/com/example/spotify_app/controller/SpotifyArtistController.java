package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.service.TokenService;
import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Me.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifyArtistController {

    private final TokenService tokenService;

    public SpotifyArtistController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/me/top/artists")
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

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String artistId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = String.format("/artists/%s", artistId);

        return tokenService.makeRequest(userId, endpoint, Artist.class);
    }

    @GetMapping("/artists/{id}/top-tracks")
    public ResponseEntity<ArtistTopTracksResponse> getTopTracksByArtist(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String artistId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String endpoint = String.format("/artists/%s/top-tracks", artistId);

        return tokenService.makeRequest(userId, endpoint.toString(), ArtistTopTracksResponse.class);
    }

    @GetMapping("/artists/{id}/albums")
    public ResponseEntity<ArtistAlbumsResponse> getAlbumsByArtist(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String artistId,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        StringBuilder endpoint = new StringBuilder(String.format("/artists/%s/albums", artistId));
        endpoint.append("?limit=").append(limit);
        endpoint.append("&offset=").append(offset);

        return tokenService.makeRequest(userId, endpoint.toString(), ArtistAlbumsResponse.class);
    }
}