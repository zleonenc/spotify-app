package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;

import com.example.spotify_app.model.Track.Track;
import com.example.spotify_app.service.SpotifyTrackService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyTrackControllerTest {

    @Mock
    private SpotifyTrackService trackService;

    private SpotifyTrackController spotifyTrackController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_TRACK_ID = "test-track-456";

    @BeforeEach
    void setUp() {
        spotifyTrackController = new SpotifyTrackController(trackService);
    }

    // Tests for getTrackById method

    @Test
    void getTrackById_ValidAuthHeaderAndTrackId_returnsTrackResponse() {
        // Given
        Track expectedTrack = new Track();
        ResponseEntity<Track> mockResponseEntity = ResponseEntity.ok(expectedTrack);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(trackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(TEST_AUTH_HEADER, TEST_TRACK_ID);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedTrack, result.getBody());
            verify(trackService).getTrackById(TEST_USER_ID, TEST_TRACK_ID);
        }
    }

    @Test
    void getTrackById_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(TEST_AUTH_HEADER, TEST_TRACK_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTrackById_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(trackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(TEST_AUTH_HEADER, TEST_TRACK_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(trackService).getTrackById(TEST_USER_ID, TEST_TRACK_ID);
        }
    }

    @Test
    void getTrackById_NullAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(null)).thenReturn(null);

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(null, TEST_TRACK_ID);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTrackById_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<Track> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(trackService.getTrackById(TEST_USER_ID, TEST_TRACK_ID)).thenReturn(errorResponse);

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(TEST_AUTH_HEADER, TEST_TRACK_ID);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            verify(trackService).getTrackById(TEST_USER_ID, TEST_TRACK_ID);
        }
    }

    @Test
    void getTrackById_AuthUtilsThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER))
                    .thenThrow(new RuntimeException("Auth error"));

            // When
            ResponseEntity<Track> result = spotifyTrackController.getTrackById(TEST_AUTH_HEADER, TEST_TRACK_ID);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        }
    }
}
