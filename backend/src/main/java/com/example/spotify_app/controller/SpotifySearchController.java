package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.model.Search.SpotifySearchResponse;
import com.example.spotify_app.service.SpotifySearchService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifySearchController {

    private final SpotifySearchService searchService;

    public SpotifySearchController(SpotifySearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<SpotifySearchResponse> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("q") String query,
            @RequestParam("type") String type,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {

        String userId = AuthUtils.extractUserId(authHeader);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            return searchService.search(userId, query, type, limit, offset);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
