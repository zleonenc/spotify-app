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

import com.example.spotify_app.model.Profile.SpotifyProfileResponse;
import com.example.spotify_app.model.Profile.SpotifyTopArtistsResponse;
import com.example.spotify_app.model.Profile.SpotifyTopTracksResponse;

@ExtendWith(MockitoExtension.class)
class SpotifyProfileServiceTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifyProfileService spotifyProfileService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final int TEST_LIMIT = 20;
    private static final String PROFILE_ENDPOINT = "/me";
    private static final String TOP_ARTISTS_ENDPOINT = "/me/top/artists?limit=20";
    private static final String TOP_TRACKS_ENDPOINT = "/me/top/tracks?limit=20";

    @BeforeEach
    void setUp() {
        spotifyProfileService = new SpotifyProfileService(spotifyApiClient);
    }

    // Tests for getProfile method

    @Test
    void getProfile_ValidUserId_returnsProfileResponse() {
        // Given
        SpotifyProfileResponse expectedResponse = new SpotifyProfileResponse();
        ResponseEntity<SpotifyProfileResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, PROFILE_ENDPOINT, SpotifyProfileResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyProfileResponse> result = spotifyProfileService.getProfile(TEST_USER_ID);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, PROFILE_ENDPOINT, SpotifyProfileResponse.class);
    }

    @Test
    void getProfile_NullUserId_delegatesToApiClient() {
        // Given
        ResponseEntity<SpotifyProfileResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, PROFILE_ENDPOINT, SpotifyProfileResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyProfileResponse> result = spotifyProfileService.getProfile(null);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, PROFILE_ENDPOINT, SpotifyProfileResponse.class);
    }

    @Test
    void getProfile_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        ResponseEntity<SpotifyProfileResponse> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, PROFILE_ENDPOINT, SpotifyProfileResponse.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<SpotifyProfileResponse> result = spotifyProfileService.getProfile(TEST_USER_ID);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, PROFILE_ENDPOINT, SpotifyProfileResponse.class);
    }

    @Test
    void getTopArtists_ValidUserIdAndLimit_returnsTopArtistsResponse() {
        // Given
        SpotifyTopArtistsResponse expectedResponse = new SpotifyTopArtistsResponse();
        ResponseEntity<SpotifyTopArtistsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, TOP_ARTISTS_ENDPOINT, SpotifyTopArtistsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileService.getTopArtists(TEST_USER_ID,
                TEST_LIMIT);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, TOP_ARTISTS_ENDPOINT, SpotifyTopArtistsResponse.class);
    }

    @Test
    void getTopArtists_DifferentLimit_buildsCorrectEndpoint() {
        // Given
        int customLimit = 10;
        String expectedEndpoint = "/me/top/artists?limit=10";
        SpotifyTopArtistsResponse expectedResponse = new SpotifyTopArtistsResponse();
        ResponseEntity<SpotifyTopArtistsResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifyTopArtistsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileService.getTopArtists(TEST_USER_ID,
                customLimit);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifyTopArtistsResponse.class);
    }

    @Test
    void getTopArtists_NullUserId_delegatesToApiClient() {
        // Given
        ResponseEntity<SpotifyTopArtistsResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, TOP_ARTISTS_ENDPOINT, SpotifyTopArtistsResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopArtistsResponse> result = spotifyProfileService.getTopArtists(null, TEST_LIMIT);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, TOP_ARTISTS_ENDPOINT, SpotifyTopArtistsResponse.class);
    }

    @Test
    void getTopTracks_ValidUserIdAndLimit_returnsTopTracksResponse() {
        // Given
        SpotifyTopTracksResponse expectedResponse = new SpotifyTopTracksResponse();
        ResponseEntity<SpotifyTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, TOP_TRACKS_ENDPOINT, SpotifyTopTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileService.getTopTracks(TEST_USER_ID, TEST_LIMIT);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, TOP_TRACKS_ENDPOINT, SpotifyTopTracksResponse.class);
    }

    @Test
    void getTopTracks_DifferentLimit_buildsCorrectEndpoint() {
        // Given
        int customLimit = 10;
        String expectedEndpoint = "/me/top/tracks?limit=10";
        SpotifyTopTracksResponse expectedResponse = new SpotifyTopTracksResponse();
        ResponseEntity<SpotifyTopTracksResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifyTopTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileService.getTopTracks(TEST_USER_ID, customLimit);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifyTopTracksResponse.class);
    }

    @Test
    void getTopTracks_NullUserId_delegatesToApiClient() {
        // Given
        ResponseEntity<SpotifyTopTracksResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, TOP_TRACKS_ENDPOINT, SpotifyTopTracksResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifyTopTracksResponse> result = spotifyProfileService.getTopTracks(null, TEST_LIMIT);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, TOP_TRACKS_ENDPOINT, SpotifyTopTracksResponse.class);
    }
}
