package com.example.spotify_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.spotify_app.model.Search.SpotifySearchResponse;

@Service
public class SpotifySearchService {

    private final SpotifyApiClient tokenService;

    public SpotifySearchService(SpotifyApiClient tokenService) {
        this.tokenService = tokenService;
    }

    public ResponseEntity<SpotifySearchResponse> search(String userId, String query, String type, Integer limit,
            Integer offset) {
        StringBuilder endpoint = new StringBuilder("/search");

        try {
            endpoint.append("?q=").append(java.net.URLEncoder.encode(query, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding search query", e);
        }
        endpoint.append("&type=").append(type);
        endpoint.append("&limit=").append(limit);
        endpoint.append("&offset=").append(offset);

        return tokenService.makeRequest(userId, endpoint.toString(), SpotifySearchResponse.class);
    }
}
