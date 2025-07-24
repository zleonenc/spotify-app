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

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Album.AlbumTracksResponse;
import com.example.spotify_app.service.SpotifyAlbumService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
@Validated
public class SpotifyAlbumController {

    private final SpotifyAlbumService albumService;

    public SpotifyAlbumController(SpotifyAlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbumById(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Album ID must not be empty") @PathVariable("id") String albumId) {

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
            return albumService.getAlbumById(userId, albumId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<AlbumTracksResponse> getTracksByAlbum(
            @RequestHeader("Authorization") String authHeader,
            @NotNull @Size(min = 1, message = "Album ID must not be empty") @PathVariable("id") String albumId) {

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
            return albumService.getAlbumTracks(userId, albumId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}