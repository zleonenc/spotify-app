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

import com.example.spotify_app.model.Search.SpotifySearchResponse;
import com.example.spotify_app.service.SpotifySearchService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifySearchControllerTest {

    @Mock
    private SpotifySearchService searchService;

    private SpotifySearchController spotifySearchController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_QUERY = "test query";
    private static final String TEST_TYPE = "track";
    private static final Integer TEST_LIMIT = 20;
    private static final Integer TEST_OFFSET = 0;

    @BeforeEach
    void setUp() {
        spotifySearchController = new SpotifySearchController(searchService);
    }

    // Tests for search method

    @Test
    void search_ValidParametersDefaultLimitOffset_returnsSearchResponse() {
        // Given
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(searchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET))
                    .thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(mockResponseEntity, result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expectedResponse, result.getBody());
            verify(searchService).search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET);
        }
    }

    @Test
    void search_CustomLimitAndOffset_callsServiceWithCustomParameters() {
        // Given
        Integer customLimit = 10;
        Integer customOffset = 5;
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(searchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, customLimit, customOffset))
                    .thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, customLimit, customOffset);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(searchService).search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, customLimit, customOffset);
        }
    }

    @Test
    void search_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void search_NullAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(null)).thenReturn(null);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(null, TEST_QUERY, TEST_TYPE,
                    TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void search_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(searchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET))
                    .thenThrow(new RuntimeException("Service error"));

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(searchService).search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET);
        }
    }

    @Test
    void search_ServiceReturnsError_returnsServiceError() {
        // Given
        ResponseEntity<SpotifySearchResponse> errorResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(searchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET))
                    .thenReturn(errorResponse);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(errorResponse, result);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            verify(searchService).search(TEST_USER_ID, TEST_QUERY, TEST_TYPE, TEST_LIMIT, TEST_OFFSET);
        }
    }

    // Test different search types

    @Test
    void search_ArtistType_callsServiceWithArtistType() {
        // Given
        String artistType = "artist";
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);

        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            when(searchService.search(TEST_USER_ID, TEST_QUERY, artistType, TEST_LIMIT, TEST_OFFSET))
                    .thenReturn(mockResponseEntity);

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    artistType, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(mockResponseEntity, result);
            verify(searchService).search(TEST_USER_ID, TEST_QUERY, artistType, TEST_LIMIT, TEST_OFFSET);
        }
    }

    @Test
    void search_AuthUtilsThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER))
                    .thenThrow(new RuntimeException("Auth error"));

            // When
            ResponseEntity<SpotifySearchResponse> result = spotifySearchController.search(TEST_AUTH_HEADER, TEST_QUERY,
                    TEST_TYPE, TEST_LIMIT, TEST_OFFSET);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        }
    }

}
