package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.model.Album.Album;
import com.example.spotify_app.model.Album.AlbumTracksResponse;
import com.example.spotify_app.service.SpotifyAlbumService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifyAlbumController {

    private final SpotifyAlbumService albumService;

    public SpotifyAlbumController(SpotifyAlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbumById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String albumId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return albumService.getAlbumById(userId, albumId);
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<AlbumTracksResponse> getTracksByAlbum(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String albumId) {
        String userId = AuthUtils.extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return albumService.getAlbumTracks(userId, albumId);
    }
}