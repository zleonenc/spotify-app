package com.example.spotify_app.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.spotify_app.model.SpotifyTokenResponse;
import java.util.Map;

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
