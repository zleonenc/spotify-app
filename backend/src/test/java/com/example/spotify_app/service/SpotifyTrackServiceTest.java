package com.example.spotify_app.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.example.spotify_app.model.Track.Track;

@ExtendWith(MockitoExtension.class)
class SpotifyTrackServiceTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifyTrackService spotifyTrackService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_TRACK_ID = "test-track-456";

    @BeforeEach
    void setUp() {
        spotifyTrackService = new SpotifyTrackService(spotifyApiClient);
    }

    // Tests for getTrackById method

    @Test
    void getTrackById_ValidUserIdAndTrackId_returnsTrackResponse() {
        // Given
        String expectedEndpoint = "/tracks/test-track-456";
        Track expectedTrack = new Track();
        ResponseEntity<Track> mockResponseEntity = ResponseEntity.ok(expectedTrack);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Track.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedTrack, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Track.class);
    }

    @Test
    void getTrackById_SpecialCharactersInTrackId_buildsCorrectEndpoint() {
        // Given
        String specialTrackId = "track-with-special!@#$%^&*()";
        String expectedEndpoint = "/tracks/track-with-special!@#$%^&*()";
        ResponseEntity<Track> mockResponseEntity = ResponseEntity.ok(new Track());
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Track.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(TEST_USER_ID, specialTrackId);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Track.class);
    }

    @Test
    void getTrackById_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/tracks/test-track-456";
        ResponseEntity<Track> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, Track.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(null, TEST_TRACK_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, Track.class);
    }

    @Test
    void getTrackById_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/tracks/test-track-456";
        ResponseEntity<Track> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Track.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Track.class);
    }

    @Test
    void getTrackById_ApiClientReturnsUnauthorized_returnsUnauthorizedResponse() {
        // Given
        String expectedEndpoint = "/tracks/test-track-456";
        ResponseEntity<Track> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Track.class))
                .thenReturn(unauthorizedResponse);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID);

        // Then
        assertEquals(unauthorizedResponse, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Track.class);
    }

    @Test
    void getTrackById_ApiClientReturnsInternalServerError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/tracks/test-track-456";
        ResponseEntity<Track> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, Track.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<Track> result = spotifyTrackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, Track.class);
    }
}
