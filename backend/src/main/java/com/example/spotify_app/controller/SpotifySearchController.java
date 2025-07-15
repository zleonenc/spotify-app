package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spotify_app.service.TokenService;
import com.example.spotify_app.model.SpotifySearchResponse;
import com.example.spotify_app.util.AuthUtils;

@RestController
@RequestMapping("/api")
public class SpotifySearchController {

    private final TokenService tokenService;

    public SpotifySearchController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/search")
    public ResponseEntity<SpotifySearchResponse> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("q") String query,
            @RequestParam("type") String type,
            @RequestParam(value = "market", defaultValue = "MX") String market,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {

        String userId = AuthUtils.extractUserId(authHeader);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        StringBuilder endpoint = new StringBuilder("/search");

        try {
            endpoint.append("?q=").append(java.net.URLEncoder.encode(query, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        endpoint.append("&type=").append(type);
        endpoint.append("&market=").append(market);
        endpoint.append("&limit=").append(limit);
        endpoint.append("&offset=").append(offset);

        return tokenService.makeRequest(userId, endpoint.toString(), SpotifySearchResponse.class);
    }
}
