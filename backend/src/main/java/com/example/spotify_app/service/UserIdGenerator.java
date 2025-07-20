package com.example.spotify_app.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserIdGenerator {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }
}