package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import com.example.spotify_app.service.SpotifyArtistService;
import com.example.spotify_app.model.Artist.Artist;
import com.example.spotify_app.model.Artist.ArtistTopTracksResponse;
import com.example.spotify_app.model.Artist.ArtistAlbumsResponse;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
@Validated
public class SpotifyArtistController {

    private final SpotifyArtistService artistService;

    public SpotifyArtistController(SpotifyArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Artist ID must not be empty") @PathVariable("id") String artistId) {

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
            return artistService.getArtistById(userId, artistId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/artists/{id}/top-tracks")
    public ResponseEntity<ArtistTopTracksResponse> getTopTracksByArtist(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Artist ID must not be empty") @PathVariable("id") String artistId) {

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
            return artistService.getTopTracks(userId, artistId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/artists/{id}/albums")
    public ResponseEntity<ArtistAlbumsResponse> getAlbumsByArtist(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Artist ID must not be empty") @PathVariable("id") String artistId,
            @RequestParam(value = "limit", defaultValue = "20") @Min(value = 1, message = "Limit must be at least 1") @Max(value = 50, message = "Limit cannot exceed 50") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") @Min(value = 0, message = "Offset must be at least 0") Integer offset) {

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
            return artistService.getAlbums(userId, artistId, limit, offset);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}