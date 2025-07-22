package com.example.spotify_app.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class UserIdGeneratorTest {

    @Test
    void generateUserId_returnsValidUUID() {
        // Given
        // Not required

        // When
        String result = UserIdGenerator.generateUserId();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertDoesNotThrow(() -> UUID.fromString(result));
    }

    @Test
    void generateUserId_returnsUniqueIds() {
        // Given
        Set<String> generatedIds = new HashSet<>();
        int numberOfGenerations = 1000;

        // When
        for (int i = 0; i < numberOfGenerations; i++) {
            String userId = UserIdGenerator.generateUserId();
            generatedIds.add(userId);
        }

        // Then
        assertEquals(numberOfGenerations, generatedIds.size(),
                "All generated user IDs should be unique");
    }

    @Test
    void generateUserId_returnsStringFormat() {
        // Given
        // Not required

        // When
        String result = UserIdGenerator.generateUserId();

        // Then
        assertTrue(result instanceof String);
        assertTrue(result.matches(
                "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"),
                "Generated ID should match UUID format");
    }

    @Test
    void generateUserId_hasConsistentLength() {
        // Given
        int expectedLength = 36; // Standard UUID string length

        // When
        String result1 = UserIdGenerator.generateUserId();
        String result2 = UserIdGenerator.generateUserId();
        String result3 = UserIdGenerator.generateUserId();

        // Then
        assertEquals(expectedLength, result1.length());
        assertEquals(expectedLength, result2.length());
        assertEquals(expectedLength, result3.length());
    }

    @Test
    void generateUserId_multipleCallsReturnDifferentValues() {
        // Given
        // Not required

        // When
        String userId1 = UserIdGenerator.generateUserId();
        String userId2 = UserIdGenerator.generateUserId();
        String userId3 = UserIdGenerator.generateUserId();

        // Then
        assertNotEquals(userId1, userId2);
        assertNotEquals(userId1, userId3);
        assertNotEquals(userId2, userId3);
    }
}
