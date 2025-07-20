package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.service.SpotifyArtistService;
import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifyArtistController {

    private final SpotifyArtistService artistService;

    public SpotifyArtistController(SpotifyArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String artistId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return artistService.getArtistById(userId, artistId);
    }

    @GetMapping("/artists/{id}/top-tracks")
    public ResponseEntity<ArtistTopTracksResponse> getTopTracksByArtist(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String artistId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return artistService.getTopTracks(userId, artistId);
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

        return artistService.getAlbums(userId, artistId, limit, offset);
    }
}