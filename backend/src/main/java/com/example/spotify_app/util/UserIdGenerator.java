package com.example.spotify_app.util;

import java.util.UUID;

public class UserIdGenerator {

    private UserIdGenerator() {
    }

    public static String generateUserId() {
        return UUID.randomUUID().toString();
    }
}
