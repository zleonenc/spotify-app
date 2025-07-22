package com.example.spotify_app.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthUtilsTest {

    @Test
    void extractUserId_ValidBearerToken_returnsUserId() {
        // Given
        String authHeader = "Bearer user123";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertEquals("user123", result);
    }

    @Test
    void extractUserId_ValidBearerTokenWithSpaces_returnsUserIdWithSpaces() {
        // Given
        String authHeader = "Bearer user with spaces";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertEquals("user with spaces", result);
    }

    @Test
    void extractUserId_BearerWithSpecialCharacters_returnsUserIdWithSpecialCharacters() {
        // Given
        String authHeader = "Bearer user@áé12%$3.com";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertEquals("user@áé12%$3.com", result);
    }

    @Test
    void extractUserId_NullAuthHeader_returnsNull() {
        // Given
        String authHeader = null;

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }

    @Test
    void extractUserId_EmptyAuthHeader_returnsNull() {
        // Given
        String authHeader = "";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }

    @Test
    void extractUserId_InvalidPrefix_returnsNull() {
        // Given
        String authHeader = "Basic user123";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }

    @Test
    void extractUserId_BearerWithoutSpace_returnsNull() {
        // Given
        String authHeader = "Beareruser123";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }

    @Test
    void extractUserId_OnlyBearer_returnsNull() {
        // Given
        String authHeader = "Bearer";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }

    @Test
    void extractUserId_CaseSensitiveBearer_returnsNull() {
        // Given
        String authHeader = "bearer user123";

        // When
        String result = AuthUtils.extractUserId(authHeader);

        // Then
        assertNull(result);
    }
}
