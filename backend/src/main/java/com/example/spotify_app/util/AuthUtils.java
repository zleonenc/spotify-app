package com.example.spotify_app.util;

public class AuthUtils {

    private AuthUtils() {
    }

    public static String extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}