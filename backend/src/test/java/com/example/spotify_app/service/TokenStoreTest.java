package com.example.spotify_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.spotify_app.model.SpotifyTokenResponse;

class TokenStoreTest {

    private TokenStore tokenStore;

    private static final String TOKEN_TYPE = "Bearer";
    private static final String SCOPES = "user-read-private user-read-email";
    private static final int EXPIRES_IN = 3600;

    @BeforeEach
    void setUp() {
        tokenStore = new TokenStore();
    }

    // Tests for saveToken method

    @Test
    void saveToken_ValidUserIdAndToken_savesToken() {
        // Given
        String userId = "user123";
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);

        // When
        tokenStore.saveToken(userId, tokenResponse);

        // Then
        SpotifyTokenResponse retrievedToken = tokenStore.getToken(userId);
        assertNotNull(retrievedToken);
        assertEquals(tokenResponse.getAccessToken(), retrievedToken.getAccessToken());
        assertEquals(tokenResponse.getTokenType(), retrievedToken.getTokenType());
        assertEquals(tokenResponse.getRefreshToken(), retrievedToken.getRefreshToken());
        assertEquals(tokenResponse.getExpiresIn(), retrievedToken.getExpiresIn());
        assertEquals(tokenResponse.getScope(), retrievedToken.getScope());
    }

    @Test
    void saveToken_NullUserId_throwsNullPointerException() {
        // Given
        String userId = null;
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            tokenStore.saveToken(userId, tokenResponse);
        });
    }

    @Test
    void saveToken_NullToken_throwsNullPointerException() {
        // Given
        String userId = "user123";
        SpotifyTokenResponse tokenResponse = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            tokenStore.saveToken(userId, tokenResponse);
        });
    }

    @Test
    void saveToken_OverwriteExistingToken_replacesToken() {
        // Given
        String userId = "user123";
        SpotifyTokenResponse firstToken = new SpotifyTokenResponse(
                "first_access_token",
                TOKEN_TYPE,
                "first_refresh_token",
                EXPIRES_IN,
                SCOPES);
        SpotifyTokenResponse secondToken = new SpotifyTokenResponse(
                "second_access_token",
                TOKEN_TYPE,
                "second_refresh_token",
                7200,
                "user-read-private user-read-email");

        // When
        tokenStore.saveToken(userId, firstToken);
        tokenStore.saveToken(userId, secondToken);

        // Then
        SpotifyTokenResponse retrievedToken = tokenStore.getToken(userId);
        assertNotNull(retrievedToken);
        assertEquals(secondToken.getAccessToken(), retrievedToken.getAccessToken());
        assertEquals(secondToken.getRefreshToken(), retrievedToken.getRefreshToken());
        assertEquals(secondToken.getExpiresIn(), retrievedToken.getExpiresIn());
        assertNotEquals(firstToken.getAccessToken(), retrievedToken.getAccessToken());
    }

    // Tests for getToken method

    @Test
    void getToken_ExistingUserId_returnsToken() {
        // Given
        String userId = "user123";
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);
        tokenStore.saveToken(userId, tokenResponse);

        // When
        SpotifyTokenResponse result = tokenStore.getToken(userId);

        // Then
        assertNotNull(result);
        assertEquals(tokenResponse, result);
    }

    @Test
    void getToken_NonExistentUserId_returnsNull() {
        // Given
        String userId = "nonexistent_user";

        // When
        SpotifyTokenResponse result = tokenStore.getToken(userId);

        // Then
        assertNull(result);
    }

    @Test
    void getToken_NullUserId_throwsNullPointerException() {
        // Given
        String userId = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            tokenStore.getToken(userId);
        });
    }

    @Test
    void getToken_EmptyUserId_returnsNull() {
        // Given
        String userId = "";

        // When
        SpotifyTokenResponse result = tokenStore.getToken(userId);

        // Then
        assertNull(result);
    }

    // Tests for removeToken method

    @Test
    void removeToken_ExistingUserId_removesToken() {
        // Given
        String userId = "user123";
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);
        tokenStore.saveToken(userId, tokenResponse);

        // When
        tokenStore.removeToken(userId);

        // Then
        SpotifyTokenResponse retrievedToken = tokenStore.getToken(userId);
        assertNull(retrievedToken);
    }

    @Test
    void removeToken_NonExistentUserId_noEffect() {
        // Given
        String existingUserId = "user123";
        String nonExistentUserId = "nonexistent_user";
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);
        tokenStore.saveToken(existingUserId, tokenResponse);

        // When
        tokenStore.removeToken(nonExistentUserId);

        // Then
        SpotifyTokenResponse existingToken = tokenStore.getToken(existingUserId);
        assertNotNull(existingToken);
        assertEquals(tokenResponse, existingToken);
    }

    @Test
    void removeToken_NullUserId_throwsNullPointerException() {
        // Given
        String existingUserId = "user123";
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                EXPIRES_IN,
                SCOPES);
        tokenStore.saveToken(existingUserId, tokenResponse);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            tokenStore.removeToken(null);
        });

        // Verify existing token is still there
        SpotifyTokenResponse existingToken = tokenStore.getToken(existingUserId);
        assertNotNull(existingToken);
        assertEquals(tokenResponse, existingToken);
    }
}
