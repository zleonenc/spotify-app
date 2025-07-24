package com.example.spotify_app.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.example.spotify_app.model.SpotifyTokenResponse;

@Component
public class TokenStore {
    private final Map<String, SpotifyTokenResponse> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String userId, SpotifyTokenResponse tokenResponse) {
        tokenStore.put(userId, tokenResponse);
    }

    public SpotifyTokenResponse getToken(String userId) {
        return tokenStore.get(userId);
    }

    public void removeToken(String userId) {
        tokenStore.remove(userId);
    }
}
