package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import com.example.spotify_app.model.Search.SpotifySearchResponse;
import com.example.spotify_app.service.SpotifySearchService;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
@Validated
public class SpotifySearchController {

    private final SpotifySearchService searchService;

    public SpotifySearchController(SpotifySearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<SpotifySearchResponse> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("q") @NotNull @Size(min = 1) String query,
            @RequestParam("type") @NotNull @Size(min = 1) String type,
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
            return searchService.search(userId, query, type, limit, offset);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
