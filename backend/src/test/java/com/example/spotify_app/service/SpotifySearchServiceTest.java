package com.example.spotify_app.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.example.spotify_app.model.Search.SpotifySearchResponse;

@ExtendWith(MockitoExtension.class)
class SpotifySearchServiceTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifySearchService spotifySearchService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_QUERY = "test query";
    private static final String TEST_TYPE = "track";
    private static final Integer TEST_LIMIT = 20;
    private static final Integer TEST_OFFSET = 0;

    @BeforeEach
    void setUp() {
        spotifySearchService = new SpotifySearchService(spotifyApiClient);
    }

    // Tests for search method

    @Test
    void search_ValidParameters_returnsSearchResponse() {
        // Given
        String expectedEndpoint = "/search?q=test+query&type=track&limit=20&offset=0";
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE,
                TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class);
    }

    @Test
    void search_DifferentQueryType_buildsCorrectEndpoint() {
        // Given
        String customQuery = "artist name";
        String customType = "artist";
        String expectedEndpoint = "/search?q=artist+name&type=artist&limit=20&offset=0";
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(TEST_USER_ID, customQuery,
                customType, TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class);
    }

    @Test
    void search_DifferentLimitAndOffset_buildsCorrectEndpoint() {
        // Given
        Integer customLimit = 50;
        Integer customOffset = 10;
        String expectedEndpoint = "/search?q=test+query&type=track&limit=50&offset=10";
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE,
                customLimit, customOffset);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class);
    }

    @Test
    void search_NullUserId_delegatesToApiClient() {
        // Given
        String expectedEndpoint = "/search?q=test+query&type=track&limit=20&offset=0";
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        when(spotifyApiClient.makeRequest(null, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(null, TEST_QUERY, TEST_TYPE,
                TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(null, expectedEndpoint, SpotifySearchResponse.class);
    }

    @Test
    void search_NullQuery_throwsRuntimeException() {
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            spotifySearchService.search(TEST_USER_ID, null, TEST_TYPE, TEST_LIMIT, TEST_OFFSET);
        });

        verifyNoInteractions(spotifyApiClient);
    }

    @Test
    void search_MultipleSearchTypes_buildsCorrectEndpoint() {
        // Given
        String multipleTypes = "track,artist,album";
        String expectedEndpoint = "/search?q=test+query&type=track,artist,album&limit=20&offset=0";
        SpotifySearchResponse expectedResponse = new SpotifySearchResponse();
        ResponseEntity<SpotifySearchResponse> mockResponseEntity = ResponseEntity.ok(expectedResponse);
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(mockResponseEntity);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(TEST_USER_ID, TEST_QUERY,
                multipleTypes, TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(mockResponseEntity, result);
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class);
    }

    @Test
    void search_ApiClientReturnsError_returnsErrorResponse() {
        // Given
        String expectedEndpoint = "/search?q=test+query&type=track&limit=20&offset=0";
        ResponseEntity<SpotifySearchResponse> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        when(spotifyApiClient.makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class))
                .thenReturn(errorResponse);

        // When
        ResponseEntity<SpotifySearchResponse> result = spotifySearchService.search(TEST_USER_ID, TEST_QUERY, TEST_TYPE,
                TEST_LIMIT, TEST_OFFSET);

        // Then
        assertEquals(errorResponse, result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(spotifyApiClient).makeRequest(TEST_USER_ID, expectedEndpoint, SpotifySearchResponse.class);
    }
}
