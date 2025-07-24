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

import com.example.spotify_app.model.Profile.SpotifyProfileResponse;
import com.example.spotify_app.model.Profile.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Profile.SpotifyTopTracksResponse;
import com.example.spotify_app.service.SpotifyProfileService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyProfileControllerTest {

    @Mock
    private SpotifyProfileService profileService;

    private SpotifyProfileController spotifyProfileController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final Integer TEST_LIMIT = 20;

    @BeforeEach
    void setUp() {
        spotifyProfileController = new SpotifyProfileController(profileService);
    }

    // Tests for getProfile method

    @Test
    void getProfile_ValidAuthHeader_returnsProfileResponse() {
        // Given
        SpotifyProfileResponse expectedResponse = new SpotifyProfileResponse();
        ResponseEntity<SpotifyProfileResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getProfile(TEST_USER_ID)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifyProfileResponse> result = spotifyProfileController.getProfile(TEST_AUTH_HEADER);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(profileService).getProfile(TEST_USER_ID);
        }
    }

    @Test
    void getProfile_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<SpotifyProfileResponse> result = spotifyProfileController.getProfile(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getProfile_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getProfile(TEST_USER_ID)).thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<SpotifyProfileResponse> result = spotifyProfileController.getProfile(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(profileService).getProfile(TEST_USER_ID);
        }
    }

    @Test
    void getProfile_NullAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(null)).thenReturn(null);

            // When
            ResponseEntity<SpotifyProfileResponse> result = spotifyProfileController.getProfile(null);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    // Tests for getTopArtists method

    @Test
    void getTopArtists_ValidAuthHeaderAndLimit_returnsTopArtistsResponse() {
        // Given
        SpotifyTopArtistsResponse expectedResponse = new SpotifyTopArtistsResponse();
        ResponseEntity<SpotifyTopArtistsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopArtists(TEST_USER_ID, TEST_LIMIT)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileController.getTopArtists(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(profileService).getTopArtists(TEST_USER_ID, TEST_LIMIT);
        }
    }

    @Test
    void getTopArtists_CustomLimit_callsServiceWithCustomLimit() {
        // Given
        Integer customLimit = 10;
        SpotifyTopArtistsResponse expectedResponse = new SpotifyTopArtistsResponse();
        ResponseEntity<SpotifyTopArtistsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopArtists(TEST_USER_ID, customLimit)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileController.getTopArtists(TEST_AUTH_HEADER,
                    customLimit);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(profileService).getTopArtists(TEST_USER_ID, customLimit);
        }
    }

    @Test
    void getTopArtists_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileController.getTopArtists(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTopArtists_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopArtists(TEST_USER_ID, TEST_LIMIT))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileController.getTopArtists(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(profileService).getTopArtists(TEST_USER_ID, TEST_LIMIT);
        }
    }

    // Tests for getTopTracks method

    @Test
    void getTopTracks_ValidAuthHeaderAndLimit_returnsTopTracksResponse() {
        // Given
        SpotifyTopTracksResponse expectedResponse = new SpotifyTopTracksResponse();
        ResponseEntity<SpotifyTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopTracks(TEST_USER_ID, TEST_LIMIT)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileController.getTopTracks(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(profileService).getTopTracks(TEST_USER_ID, TEST_LIMIT);
        }
    }

    @Test
    void getTopTracks_CustomLimit_callsServiceWithCustomLimit() {
        // Given
        Integer customLimit = 30;
        SpotifyTopTracksResponse expectedResponse = new SpotifyTopTracksResponse();
        ResponseEntity<SpotifyTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopTracks(TEST_USER_ID, customLimit)).thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileController.getTopTracks(TEST_AUTH_HEADER,
                    customLimit);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(profileService).getTopTracks(TEST_USER_ID, customLimit);
        }
    }

    @Test
    void getTopTracks_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileController.getTopTracks(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void getTopTracks_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopTracks(TEST_USER_ID, TEST_LIMIT))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileController.getTopTracks(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(profileService).getTopTracks(TEST_USER_ID, TEST_LIMIT);
        }
    }

    @Test
    void getTopTracks_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<SpotifyTopTracksResponse> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(profileService.getTopTracks(TEST_USER_ID, TEST_LIMIT)).thenReturn(errorResponse);

            // When
            ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileController.getTopTracks(TEST_AUTH_HEADER,
                    TEST_LIMIT);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            verify(profileService).getTopTracks(TEST_USER_ID, TEST_LIMIT);
        }
    }
}
